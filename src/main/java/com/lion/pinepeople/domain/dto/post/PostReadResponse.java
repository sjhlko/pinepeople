package com.lion.pinepeople.domain.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lion.pinepeople.domain.entity.Post;
import lombok.*;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;

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
    private Long hits;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp updatedAt;

    public static PostReadResponse of (Post post) {

        return PostReadResponse.builder()
                .id(post.getId())
                .userName(post.getUser().getName())
                .title(post.getTitle())
                .body(post.getBody())
                .hits(post.getHits())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }


    public static Page<PostReadResponse> of (Page<Post> posts) {

        return posts.map(map -> PostReadResponse.builder()
                .id(map.getId())
                .title(map.getTitle())
                .body(map.getBody())
                .userName(map.getUser().getName())
                .createdAt(map.getCreatedAt())
                .updatedAt(map.getUpdatedAt())
                .build()
        );
    }
    
}