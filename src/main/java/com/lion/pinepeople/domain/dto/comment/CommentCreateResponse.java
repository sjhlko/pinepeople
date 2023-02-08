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


    public static CommentCreateResponse of (Comment savedComment) {
        return CommentCreateResponse.builder()
                .id(savedComment.getId())
                .build();
    }

}
