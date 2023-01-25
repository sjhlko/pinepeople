package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.user.delete.UserDeleteResponse;
import com.lion.pinepeople.domain.dto.user.join.UserJoinRequest;
import com.lion.pinepeople.domain.dto.user.join.UserJoinResponse;
import com.lion.pinepeople.domain.dto.user.login.UserLoginRequest;
import com.lion.pinepeople.domain.dto.user.login.UserLoginResponse;
import com.lion.pinepeople.domain.dto.user.myInfo.MyInfoResponse;
import com.lion.pinepeople.domain.dto.user.update.UserUpdateRequest;
import com.lion.pinepeople.domain.dto.user.update.UserUpdateResponse;
import com.lion.pinepeople.domain.dto.user.role.UserRoleResponse;
import com.lion.pinepeople.domain.dto.user.userInfo.UserInfoResponse;
import com.lion.pinepeople.domain.dto.user.userInfoList.UserInfoListResponse;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.enums.UserRole;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.UserRepository;
import com.lion.pinepeople.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final BrixService brixService;
    @Value("${jwt.token.secret}")
    private String key;

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
        User user = User.of(userJoinRequest, encoder.encode(userJoinRequest.getPassword()));
        //User 저장
        user = userRepository.save(user);
        brixService.setBrix(user);
        //User -> UserJoinResponse 변환 후 반환
        return UserJoinResponse.of(user.getId(), user.getName());
    }

    /**
     * 로그인 메서드
     *
     * @param userLoginRequest email, password
     * @return UserLoginResponse jwt
     */
    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        final long expireTimeMs = 1000 * 60 * 60L;
        //이메일 체크
        User findUser = userRepository.findByEmail(userLoginRequest.getEmail()).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        //비밀번호 체크
        if (!encoder.matches(userLoginRequest.getPassword(), findUser.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASSWORD.getMessage());
        }
        //토큰 발행
        String token = JwtTokenUtil.createToken(findUser.getId(), key, expireTimeMs);
        return UserLoginResponse.of(token);
    }



    /**
     * 유저 수정 메서드
     *
     * @param userId            수정하려고 하는 유저 id
     * @param userUpdateRequest name, address, phone, birth
     * @return UserUpdateResponse message, userName
     */
    public UserUpdateResponse modify(String userId, UserUpdateRequest userUpdateRequest) {
        // 수정을 하는 유저 체크
        User updateUser = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다.");
        });
        // 유저 수정
        updateUser.updateUser(userUpdateRequest);
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
}
