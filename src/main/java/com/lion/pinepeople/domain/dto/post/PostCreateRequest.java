package com.lion.pinepeople.domain.dto.post;


import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.User;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String body;


    public Post of(User user) {
        return Post.builder()
                .title(this.title)
                .body(this.body)
                .user(user)
                .build();
    }

}

