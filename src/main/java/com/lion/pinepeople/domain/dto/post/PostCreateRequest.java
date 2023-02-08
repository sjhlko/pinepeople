package com.lion.pinepeople.domain.dto.post;


import com.lion.pinepeople.domain.entity.Post;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {


    @NotBlank(message = "제목을 입력해 주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해 주세요.")
    private String body;


    public Post of() {
        return Post.builder()
                .title(this.title)
                .body(this.body)
                .build();
    }

}

