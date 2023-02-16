package com.lion.pinepeople.domain.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lion.pinepeople.domain.entity.Post;
import lombok.*;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;
import java.util.Optional;

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
    private int hits;
    private Integer commentsCount;
    private Integer recommendsCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp updatedAt;

    public static PostReadResponse of (Optional<Post> post) {
        return PostReadResponse.builder()
                .id(post.get().getId())
                .userName(post.get().getUser().getName())
                .title(post.get().getTitle())
                .body(post.get().getBody())
                .commentsCount(post.get().getCommentsCount())
                .recommendsCount(post.get().getRecommendsCount())
                .hits(post.get().getHits())
                .createdAt(post.get().getCreatedAt())
                .updatedAt(post.get().getUpdatedAt())
                .build();
    }


    public static Page<PostReadResponse> of (Page<Post> posts) {
        return posts.map(map -> PostReadResponse.builder()
                .id(map.getId())
                .title(map.getTitle())
                .body(map.getBody())
                .commentsCount(map.getCommentsCount())
                .recommendsCount(map.getRecommendsCount())
                .hits(map.getHits())
                .userName(map.getUser().getName())
                .build()
        );
    }

}