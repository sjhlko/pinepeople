package com.lion.pinepeople.domain.post;


import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.User;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {


     private String title;

     private String body;


    public Post of(User user) {
        return Post.builder()
                .title(this.title)
                .body(this.body)
                .user(user)
                .build();
    }

}

