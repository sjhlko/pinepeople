package com.lion.pinepeople.domain.dto.user.login;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserLoginRequest {
    private String email;
    private String password;
}
