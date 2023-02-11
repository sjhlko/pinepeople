package com.lion.pinepeople.domain.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lion.pinepeople.domain.entity.Comment;
import lombok.*;

import java.sql.Timestamp;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CommentUpdateResponse {
    private Long id;
    private String body;
    private Long userId;
    private Long postId;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp updatedAt;


    public static CommentUpdateResponse of (Comment updatedComment) {
        return CommentUpdateResponse.builder()
                .id(updatedComment.getId())
                .build();
    }


}