package com.lion.pinepeople.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDeleteResponse {

    private Long id;


    public static PostDeleteResponse of (Long postId) {
        return PostDeleteResponse.builder()
                .id(postId)
                .build();
    }

}
