package com.lion.pinepeople.domain.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lion.pinepeople.domain.entity.Comment;
import lombok.*;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CommentReadResponse {

    private Long commentId;
    private String body;
    private Long userId;
    private String userName;
    private Long postId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp createdAt;

    public static CommentReadResponse of (Comment comment) {
        return CommentReadResponse.builder()
                .commentId(comment.getId())
                .body(comment.getBody())
                .userId(comment.getUser().getId())
                .userName(comment.getUser().getName())
                .postId(comment.getPost().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public static Page<CommentReadResponse> of (Page<Comment> comments) {
        return comments.map(map -> CommentReadResponse.builder()
                .commentId(map.getId())
                .body(map.getBody())
                .userId(map.getUser().getId())
                .userName(map.getUser().getName())
                .postId(map.getPost().getId())
                .createdAt(map.getCreatedAt())
                .build());

    }

}
