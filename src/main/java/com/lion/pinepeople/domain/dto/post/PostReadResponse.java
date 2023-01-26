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


    private Long id;
    private String title;
    private String body;
    private String userName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;


    /***
     * convertToDto entity를 dto로 변환
     * @param post
     * @return
     */
    public static PostReadResponse convertToDto(Post post) {
        return PostReadResponse.builder()
                .id(post.getId())
                .userName(post.getUser().getName())
                .title(post.getTitle())
                .body(post.getBody())
                .createdAt(post.getCreatedAt().toLocalDateTime()) // () timestamp -> LocalDateTime
                .updatedAt(post.getUpdatedAt().toLocalDateTime())
                .build();
    }

    /***
     * convertListToDto
     * @param posts
     * @return
     */
    public static Page<PostReadResponse> convertListToDto(Page<Post> posts) {

        return posts.map(post -> PostReadResponse.builder()
                .id(post.getId())
                .userName(post.getUser().getName())
                .title(post.getTitle())
                .body(post.getBody())
                .createdAt(post.getCreatedAt().toLocalDateTime()) // () timestamp -> LocalDateTime
                .updatedAt(post.getUpdatedAt().toLocalDateTime())
                .build()
        );

    }
}