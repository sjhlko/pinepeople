package com.lion.pinepeople.domain.dto.user.update;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserUpdateRequest {
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;
    @NotBlank(message = "주소를 입력해주세요.")
    private String address;
    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phone;
    @NotNull(message = "생일을 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
}
