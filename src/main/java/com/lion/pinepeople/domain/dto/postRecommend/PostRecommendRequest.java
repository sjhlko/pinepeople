package com.lion.pinepeople.domain.dto.postRecommend;


import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.PostRecommend;
import com.lion.pinepeople.domain.entity.User;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRecommendRequest {


    private Long id;


     public PostRecommend of (User user, Post post) {
        return PostRecommend.builder()
                .id(this.id)
                .user(user)
                .post(post)
                .build();
    }

//    public PostRecommend of (PostRecommend postRecommend) {
//                return PostRecommend.builder()
//                .id(postRecommend.getId())
//                .user(postRecommend.getUser())
//                .post(postRecommend.getPost())
//                .build();
//    }

}
