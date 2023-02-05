package com.lion.pinepeople.domain.dto.user;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ChangePasswordRequest {
    @NotBlank(message = "email을 입력해주세요.")
    @Email
    private String email;
    @NotBlank(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = "[0-9]{10,11}", message = "-를 제외한 전화번호를 입력해주세요.")
    private String phone;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String passwordCheck;

}
