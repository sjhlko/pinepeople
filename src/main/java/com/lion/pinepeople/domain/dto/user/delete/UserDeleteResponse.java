package com.lion.pinepeople.domain.dto.user.delete;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserDeleteResponse {
    private String message;
    private Long userId;

    public static UserDeleteResponse of(String message, Long userId) {
        return UserDeleteResponse.builder()
                .message(message)
                .userId(userId)
                .build();
    }
}