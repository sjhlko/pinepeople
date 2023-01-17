package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.UserJoinRequest;
import com.lion.pinepeople.domain.dto.UserJoinResponse;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //회원가입
    public UserJoinResponse join(UserJoinRequest userJoinRequest){
        //email 중복확인
        userRepository.findByEmail(userJoinRequest.getEmail()).ifPresent(user -> {
            throw new AppException(ErrorCode.DUPLICATED_USER_NAME,ErrorCode.DUPLICATED_USER_NAME.getMessage());
        });
        //UserJoinRequest -> User

        //User 저장
        //User -> UserJoinResponse 변환 후 반환
        return null;
    }
    //로그인
    //회원 등급 변경
}
