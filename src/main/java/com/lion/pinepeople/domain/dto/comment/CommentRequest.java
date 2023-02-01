package com.lion.pinepeople.domain.dto.comment;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentRequest {

    //@NotBlank(message = "댓글을 입력해 주세요.")
    private String body;

}
