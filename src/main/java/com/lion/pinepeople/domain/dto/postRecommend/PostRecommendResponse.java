package com.lion.pinepeople.domain.dto.postRecommend;

import com.lion.pinepeople.domain.entity.PostRecommend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRecommendResponse {


    private Long id;

    public static PostRecommendResponse of(PostRecommend savedRecommend) {
        return PostRecommendResponse.builder()
                .id(savedRecommend.getId())
                .build();
    }


    public static Page<PostRecommendResponse> of(Page<PostRecommend> postRecommends) {

        return postRecommends.map(map -> PostRecommendResponse.builder()
                .id(map.getId())
                .build()
        );
    }
}