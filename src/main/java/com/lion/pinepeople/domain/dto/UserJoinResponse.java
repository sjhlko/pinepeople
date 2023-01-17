package com.lion.pinepeople.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserJoinResponse {
    private Long userId;
    private String userName;

    /**
     * 회원가입 응답 DTO
     * @param userId
     * @param userName
     * @return UserJoinResponse
     */
    public static UserJoinResponse of(Long userId, String userName) {
        return UserJoinResponse.builder()
                .userId(userId)
                .userName(userName)
                .build();
    }
}
