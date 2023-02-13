package com.lion.pinepeople.domain.dto.comment;


import com.lion.pinepeople.domain.entity.Comment;
import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.User;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentCreateRequest {


    private Long id;
    private String body;


    public Comment of(User user, Post post) {
        return Comment.builder()
                .id(this.id)
                .body(this.body)
                .user(user)
                .post(post)
                .build();
    }


}
