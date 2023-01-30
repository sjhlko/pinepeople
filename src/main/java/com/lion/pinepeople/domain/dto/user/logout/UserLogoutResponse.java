package com.lion.pinepeople.domain.dto.user.logout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserLogoutResponse {
    private String message;
}
