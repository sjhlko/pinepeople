package com.lion.pinepeople.domain.dto.comment;


import com.lion.pinepeople.domain.entity.Comment;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CommentCreateResponse {


    private Long id;
    private String body;
    private String userName;


    public static CommentCreateResponse of(Comment savedComment) {
        return CommentCreateResponse.builder()
                .id(savedComment.getId())
                .body(savedComment.getBody())
                .userName(savedComment.getUser().getName())
                .build();
    }

}
