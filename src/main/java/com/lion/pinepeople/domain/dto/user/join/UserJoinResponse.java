package com.lion.pinepeople.domain.dto.user.join;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserJoinResponse {
    private Long userId;
    private String userName;

    public static UserJoinResponse of(Long userId, String userName) {
        return UserJoinResponse.builder()
                .userId(userId)
                .userName(userName)
                .build();
    }
}
