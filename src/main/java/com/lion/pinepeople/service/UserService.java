package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.user.ChangePasswordRequest;
import com.lion.pinepeople.domain.dto.user.delete.UserDeleteResponse;
import com.lion.pinepeople.domain.dto.user.join.UserJoinRequest;
import com.lion.pinepeople.domain.dto.user.join.UserJoinResponse;
import com.lion.pinepeople.domain.dto.user.login.UserLoginRequest;
import com.lion.pinepeople.domain.dto.user.login.UserLoginResponse;
import com.lion.pinepeople.domain.dto.user.logout.UserLogoutResponse;
import com.lion.pinepeople.domain.dto.user.myInfo.MyInfoResponse;
import com.lion.pinepeople.domain.dto.user.search.UserSearchResponse;
import com.lion.pinepeople.domain.dto.user.update.UserUpdateRequest;
import com.lion.pinepeople.domain.dto.user.update.UserUpdateResponse;
import com.lion.pinepeople.domain.dto.user.userInfo.UserInfoResponse;
import com.lion.pinepeople.domain.dto.user.userInfoList.UserInfoListResponse;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.enums.UserRole;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.BlackListRepository;
import com.lion.pinepeople.repository.UserRepository;
import com.lion.pinepeople.utils.CookieUtil;
import com.lion.pinepeople.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final BrixService brixService;
    private final RedisService redisService;
    private final FileUploadService fileUploadService;
    private final BlackListRepository blackListRepository;
    private final long accessTokenExpireTimeMs = 1000 * 60 * 15L;
    private final long refreshTokenExpireTimeMs = 1000 * 60 * 60 * 24L;
    private final String dir = "profile";

    @Value("${jwt.token.secret}")
    private String key;

    private String DEFAULT_PROFILE_URL = "https://pinepeople-t3-bucket.s3.ap-northeast-2.amazonaws.com/profile/774a9913-cb42-4b24-a7fb-93156054152c-04-2.png";
    /**
     * 회원가입 메서드
     *
     * @param userJoinRequest name, email, password, address, phone, birth(yyyy-MM-dd)
     * @return UserJoinResponse userId, message
     */
    public UserJoinResponse join(UserJoinRequest userJoinRequest) {
        //email 중복확인
        userRepository.findByEmail(userJoinRequest.getEmail()).ifPresent(user -> {
            throw new AppException(ErrorCode.DUPLICATED_USER_NAME, ErrorCode.DUPLICATED_USER_NAME.getMessage());
        });

        //UserJoinRequest -> User
        User user = User.of(userJoinRequest, encoder.encode(userJoinRequest.getPassword()), DEFAULT_PROFILE_URL);

        //블랙리스트 확인
        blackListRepository.findByUser(user).ifPresent(blackList ->{
            throw new AppException(ErrorCode.SUSPENDED_USER, ErrorCode.SUSPENDED_USER.getMessage());
        });

        //User 저장
        user = userRepository.save(user);
        brixService.setBrix(user);
        //User -> UserJoinResponse 변환 후 반환
        return UserJoinResponse.of(user.getId(), user.getName());
    }

    /**
     * 로그인 메서드
     *
     * @param userLoginRequest 로그인 dto
     * @param response         쿠키를 설정하기 위해 매개변수로 받은 response
     * @return jwt를 반환한다.
     */
    public UserLoginResponse login(UserLoginRequest userLoginRequest, HttpServletResponse response) {
        //이메일 체크
        User findUser = userRepository.findByEmail(userLoginRequest.getEmail()).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        //비밀번호 체크
        if (!encoder.matches(userLoginRequest.getPassword(), findUser.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASSWORD.getMessage());
        }

        //블랙리스트 확인
        blackListRepository.findByUser(findUser).ifPresent(blackList ->{
            throw new AppException(ErrorCode.SUSPENDED_USER, ErrorCode.SUSPENDED_USER.getMessage());
        });

        //토큰 발행
        String accessToken = JwtTokenUtil.createToken(findUser.getId(), key, accessTokenExpireTimeMs);
        String refreshToken = JwtTokenUtil.createToken(findUser.getId(), key, refreshTokenExpireTimeMs);

        //레디스 저장
        redisService.setDataExpire(accessToken, refreshToken, refreshTokenExpireTimeMs / 1000);

        //쿠키 저장
        CookieUtil.saveCookie(response, "token", accessToken);

        return UserLoginResponse.of(accessToken);
    }

    /**
     * OAuth 로그인 메서드
     *
     * @param email    OAuth 로그인시 Authentcation에 올라온 emali을 통해 토큰 발행
     * @param response 리다리렉션 시 사용
     * @return access 토큰 반환
     */
    public UserLoginResponse oAuthLogin(String email, HttpServletResponse response) {
        //이메일 체크
        User findUser = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        //토큰 발행
        String accessToken = JwtTokenUtil.createToken(findUser.getId(), key, accessTokenExpireTimeMs);
        String refreshToken = JwtTokenUtil.createToken(findUser.getId(), key, refreshTokenExpireTimeMs);

        //레디스 저장
        redisService.setDataExpire(accessToken, refreshToken, refreshTokenExpireTimeMs / 1000);

        //쿠키 저장
        CookieUtil.savePathCookie(response, "token", accessToken, "/pinepeople");

        return UserLoginResponse.of(accessToken);
    }

    /**
     * 로그아웃 메서드
     *
     * @param response 쿠키를 설정하기 위해 매개변수로 받은 response
     * @return 로그아웃 성공 여부 메세지를 반환한다.
     */
    public UserLogoutResponse logout(HttpServletRequest request, HttpServletResponse response) {
        //redis aceess token logout
        String accessToken = CookieUtil.getCookieValue(request, "token");
        redisService.setDataExpire(accessToken, "LOGOUT", accessTokenExpireTimeMs / 1000);
        //쿠키 초기화
        CookieUtil.initCookie(response, "token");
        return new UserLogoutResponse("로그아웃되었습니다.");
    }

    public boolean isReissueable(HttpServletRequest request, HttpServletResponse resoponse) {
        log.info("토큰 재발급 시도");
        //accessToken 가져옴
        String accessToken = CookieUtil.getCookieValue(request, "token");
        //redis에서 refreshToken 가져오기 및 refresh 유효성 체크
        String refreshToken = redisService.getData(accessToken);
        if (refreshToken == null) {
            log.error("refresh 토큰이 없습니다.");
            return false;
        }
        if (!JwtTokenUtil.isValid(refreshToken, key).equals("OK")){
            log.error("refresh 토큰이 유효하지 않습니다.");
            return false;
        }
        Long userId = JwtTokenUtil.getUserId(refreshToken, key);
        //redis에서 기존 refresh 데이터 삭제
        redisService.deleteData(accessToken);
        //토큰 재발행 및 redis에 저장
        String newAccessToken = JwtTokenUtil.createToken(userId, key, accessTokenExpireTimeMs);
        String newRefreshToken = JwtTokenUtil.createToken(userId, key, refreshTokenExpireTimeMs);
        redisService.setDataExpire(newAccessToken, newRefreshToken, refreshTokenExpireTimeMs / 1000);
        //accessToken 쿠키에 저장
        if (request.getRequestURL().toString().contains("api"))
            CookieUtil.saveCookie(resoponse, "token", newAccessToken);
        else CookieUtil.savePathCookie(resoponse, "token", newAccessToken, "/pinepeople");
        log.info(request.getRequestURL().toString());
        log.info("토큰 재발급 성공");
        return true;
    }

    /**
     * 유저 수정 메서드
     *
     * @param userId            수정하려고 하는 유저 id
     * @param userUpdateRequest name, address, phone, birth
     * @return UserUpdateResponse message, userName
     */
    public UserUpdateResponse modify(String userId, UserUpdateRequest userUpdateRequest, MultipartFile file) throws IOException {
        // 수정을 하는 유저 체크
        User updateUser = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.");
        });
        //프로필 사진 클라우드에 업로드
        String profileImg = fileUploadService.uploadFile(file, dir);

        // 유저 수정
        updateUser.updateUser(userUpdateRequest, profileImg);
        userRepository.saveAndFlush(updateUser);
        // 유저 번경 dto 반환
        return UserUpdateResponse.of(String.format(updateUser.getName() + "님의 유저 정보가 변경되었습니다."), updateUser.getId());
    }

    /**
     * 유저 삭제 메서드
     *
     * @param userId 삭제하는 userId
     * @param id     삭제할 userid
     * @return UserDeleteResponse message userId
     */
    public UserDeleteResponse delete(String userId, Long id) {
        // 삭제를 하는 유저 체크
        User findUser = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.");
        });
        // 삭제를 하는 유저 권한 체크
        if (!(findUser.getRole().equals(UserRole.ADMIN) || findUser.getId().equals(id))) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "해당 유저 정보를 삭제할 권한이 없습니다.");
        }
        // 삭제할 대상 유저 체크
        User deleteUser = userRepository.findById(id).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "대상 유저를 찾을 수 없습니다.");
        });
        // 삭제
        String deleteMessage = String.format(deleteUser.getName() + "님의 유저 정보를 삭제했습니다.");
        Long deleteUserId = deleteUser.getId();
        userRepository.delete(deleteUser);
        // 삭제 delete dto 반환
        return UserDeleteResponse.of(deleteMessage, deleteUserId);
    }

    /**
     * 유저 상세 정보 조회 메서드
     *
     * @param id 조회할 유저 id
     * @return UserInfoResponse userId, userName, email, brixFiguer, brixName
     */
    public UserInfoResponse getUserInfo(Long id) {
        // 조회할 유저 체크
        User findUser = userRepository.findById(id).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.");
        });
        // 유저 UserInfoResponse로 변환 후 반환
        return UserInfoResponse.of(findUser);
    }

    /**
     * 유저 정보 리스트 조회
     *
     * @param pageable 페이징
     * @return Page<UserInfoList> UserInfoResponse userId, userName, email, brixFiguer, brixName
     */
    public Page<UserInfoListResponse> getUserInfoList(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return UserInfoListResponse.of(users);
    }

    /**
     * 마이페이지 메서드
     *
     * @param userId 유저 id
     * @return MyInfoResponse userId, userName, email, phone, address, birth, brixFiguer, brixName, point
     */
    public MyInfoResponse getMyInfo(String userId) {
        //유저체크
        User findUser = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.");
        });
        //MyInfoResponse 변환 후 리턴
        return MyInfoResponse.of(findUser);
    }

    public String findEmail(String phone){
        Optional<User> findUser = userRepository.findByPhone(phone);
        if(findUser.isEmpty()) {
            return "이메일을 찾을 수 없습니다.";
        }
        return "가입하신 이메일은 " + findUser.get().getEmail()+"입니다.";
    }

    public String changePassword(ChangePasswordRequest changePasswordRequest){
        //유저 존재확인
        User findUser = userRepository.findByEmail(changePasswordRequest.getEmail()).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND,"해당 email로 가입된 유저가 존재하지 않습니다.");
        });
        //번호 비교
        if(!findUser.getPhone().equals(changePasswordRequest.getPhone())){
            throw new AppException(ErrorCode.INVALID_PERMISSION,"패스워드를 변경할 권한이 없습니다.");
        }
        log.info("패스워드 : {}",changePasswordRequest.getPassword());
        //패스워드 변경
        findUser.updatePassword(encoder.encode(changePasswordRequest.getPassword()));
        userRepository.saveAndFlush(findUser);
        log.info("패스워드 변경 완료");
        return "패스워드가 변경되었습니다.";
    }

    public Page<UserSearchResponse> searchUser(String keyword, Pageable pageable){
        Page<User> users = userRepository.findDistinctByNameContainsOrEmailContains(pageable,keyword,keyword);
        Page<UserSearchResponse> userSearchResponses = UserSearchResponse.ofList(users);
        return userSearchResponses;
    }
}
