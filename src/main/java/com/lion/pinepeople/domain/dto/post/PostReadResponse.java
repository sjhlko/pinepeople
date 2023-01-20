package com.lion.pinepeople.domain.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lion.pinepeople.domain.entity.Post;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PostReadResponse {


    private Long postId;
    private String title;
    private String body;
    private String userName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;


    // entity -> dto
    public static PostReadResponse convertToDto(Post post) {
        return PostReadResponse.builder()
                .postId(post.getPostId())
                .userName(post.getUser().getName())
                .title(post.getTitle())
                .body(post.getBody())
                .createdAt(post.getCreatedAt().toLocalDateTime()) // () timestamp -> LocalDateTime
                .updatedAt(post.getUpdatedAt().toLocalDateTime())
                .build();
    }


    public static Page<PostReadResponse> ConvertListToDto(Page<Post> posts) {

        return posts.map(post -> PostReadResponse.builder()
                .postId(post.getPostId())
                .userName(post.getUser().getName())
                .title(post.getTitle())
                .body(post.getBody())
                .createdAt(post.getCreatedAt().toLocalDateTime()) // () timestamp -> LocalDateTime
                .updatedAt(post.getUpdatedAt().toLocalDateTime())
                .build()
        );

    }
}