package com.lion.pinepeople.domain.dto.post;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PostResponse {

    private String message;
    private Long postId;


    // Post(Entity) -> PostCreateResponse(DTO)
    public static PostResponse convertToDto(String message, Long postId) {
        return PostResponse.builder()
                .message(message)
                .postId(postId)
                .build();
    }
}
