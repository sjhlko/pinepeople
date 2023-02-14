package com.lion.pinepeople.domain.dto.postRecommend;

import com.lion.pinepeople.domain.dto.post.PostReadResponse;
import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.PostRecommend;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class PostRecommendResponse {


    private Long postRecommendId;
    private Integer recommendsCount;
    private String userName;


    public static PostRecommendResponse of(PostRecommend savedRecommend, Integer recommendsCount) {
        return PostRecommendResponse.builder()
                .postRecommendId(savedRecommend.getId())
                .userName(savedRecommend.getUserName())
                .recommendsCount(recommendsCount)
                  .build();
    }


//    public static Page<PostRecommendResponse> of(Page<PostRecommend> postRecommends) {
//
//        return postRecommends.map(map -> PostRecommendResponse.builder()
//                .id(map.getId())
//                .build()
//        );
//    }


}