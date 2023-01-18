package com.lion.pinepeople.domain.dto.user.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserLoginResponse {
    private String jwt;

    public static UserLoginResponse of(String token) {
        return UserLoginResponse.builder()
                .jwt(token)
                .build();
    }
}
