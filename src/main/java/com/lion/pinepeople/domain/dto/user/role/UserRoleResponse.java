package com.lion.pinepeople.domain.dto.user.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserRoleResponse {
    private String message;
    private String userName;

    public static UserRoleResponse of(String message, String userName) {
        return UserRoleResponse.builder()
                .message(message)
                .userName(userName)
                .build();
    }
}
