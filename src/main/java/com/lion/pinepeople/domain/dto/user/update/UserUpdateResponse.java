package com.lion.pinepeople.domain.dto.user.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserUpdateResponse {
    private String message;
    private Long userId;

    public static UserUpdateResponse of(String message, Long userId) {
        return UserUpdateResponse.builder()
                .message(message)
                .userId(userId)
                .build();
    }
}
