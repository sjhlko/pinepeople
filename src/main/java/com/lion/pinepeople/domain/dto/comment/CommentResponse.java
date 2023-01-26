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
public class CommentResponse {

    private Long commentId;
    private String comment;
    private Long userId;
    private Long postId; // postId

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp createdAt;


    // entity -> dto로 바꾼다
    public static CommentResponse convertToDto(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getId())
                .comment(comment.getBody())
                .userId(comment.getUser().getId())
                .postId(comment.getPost().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }


    public static Page<CommentResponse> convertListToDto(Page<Comment> commentPage) {
        return commentPage.map(map -> CommentResponse.builder()
                .commentId(map.getId())
                .comment(map.getBody())
                .userId(map.getUser().getId())
                .postId(map.getPost().getId())
                .createdAt(map.getCreatedAt())
                .build());

    }
}
