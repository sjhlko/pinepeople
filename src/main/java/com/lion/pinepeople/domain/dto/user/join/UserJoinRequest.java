package com.lion.pinepeople.domain.dto.user.join;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserJoinRequest {
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;
    @NotBlank(message = "email을 입력해주세요.")
    @Email
    private String email;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String passwordCheck;
    @NotBlank(message = "주소를 입력해주세요.")
    private String address;
    @NotBlank(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = "[0-9]{10,11}")
    private String phone;
    @NotNull(message = "생일을 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    @Email
    private String friendEmail;
}
