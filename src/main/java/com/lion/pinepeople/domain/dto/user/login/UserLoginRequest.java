package com.lion.pinepeople.domain.dto.user.login;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserLoginRequest {

    @NotBlank(message = "email을 입력해주세요.")
    @Email
    private String email;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
