package com.lion.pinepeople.domain.dto.post;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    // Validation
    //@NotBlank(message = "제목을 입력해 주세요.")
    private String title;

    //@NotBlank(message = "내용을 입력해 주세요.")
    private String body;


}
