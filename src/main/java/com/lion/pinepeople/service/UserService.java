package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.user.join.UserJoinRequest;
import com.lion.pinepeople.domain.dto.user.join.UserJoinResponse;
import com.lion.pinepeople.domain.dto.user.login.UserLoginRequest;
import com.lion.pinepeople.domain.dto.user.login.UserLoginResponse;
import com.lion.pinepeople.domain.dto.user.role.UserRoleResponse;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.enums.UserRole;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.UserRepository;
import com.lion.pinepeople.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.token.secret}")
    private String key;

    /**
     * 회원가입 메서드
     *
     * @param userJoinRequest name, email, password, address
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
     * 계정 등급 변경 메서드
     *
     * @param id     변경할 userId
     * @param userId 변경하는 userId
     * @return UserRoleResponse userName, message
     */
    public UserRoleResponse changeRole(Long id, String userId) {
        //admin 유저 체크
        User admin = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "ADMIN 유저를 찾을 수 없습니다.");
        });
        //admin 유저가 맞는지 체크
        if (!admin.getRole().equals(UserRole.ADMIN)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "계정 등급을 변경할 권한이 없습니다.");
        }
        //changeRole 대상 유저 체크
        User changeUser = userRepository.findById(id).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "계정 등급을 변경할 유저를 찾을 수 없습니다.");
        });
        //changeRole 대상 유저 role 체크
        if (changeUser.getRole().equals(UserRole.ADMIN)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "해당 계정은 ADMIN 계정입니다.");
        }
        //changeRole 대상 유저 role 수정 및 업데이트
        changeUser.updateRole(UserRole.ADMIN);
        userRepository.saveAndFlush(changeUser);
        //UserRoleResponse DTO 반환
        return UserRoleResponse.of(changeUser.getName(), "관리자로 권한이 변경되었습니다.");
    }
}
