package com.lion.pinepeople.domain.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostCreateRequest {

    private String title;
    private String body;

    // dto -> entity
    public Post toEntity(User user) {
        return Post.builder()
                .user(user)
                .title(title)
                .body(body)
                .build();
    }

}
