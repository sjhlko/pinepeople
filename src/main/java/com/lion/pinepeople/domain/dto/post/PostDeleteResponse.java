package com.lion.pinepeople.domain.dto.post;

import com.lion.pinepeople.domain.entity.Post;
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

    public static PostDeleteResponse of (Post deletedPost) {
        return PostDeleteResponse.builder()
                .id(deletedPost.getId())
                .build();
    }

}
