package com.lion.pinepeople.domain.dto.post;

import com.lion.pinepeople.domain.entity.Post;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDeleteRequest {


    private String title;

    private String body;

    public Post of() {
        return Post.builder()
                .title(this.title)
                .body(this.body)
                .build();
    }

}
