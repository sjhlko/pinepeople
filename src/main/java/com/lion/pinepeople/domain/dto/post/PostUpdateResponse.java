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
public class PostUpdateResponse {

    private Long id;


    public static PostUpdateResponse of (Post updatedPost) {
        return PostUpdateResponse.builder()
                .id(updatedPost.getId())
                .build();
    }

}
