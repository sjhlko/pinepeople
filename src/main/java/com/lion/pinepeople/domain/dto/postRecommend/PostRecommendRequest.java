package com.lion.pinepeople.domain.dto.postRecommend;


import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.PostRecommend;
import com.lion.pinepeople.domain.entity.User;
import lombok.*;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRecommendRequest {


    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;



    public PostRecommend of (Post post, User user) {
        return PostRecommend.builder()
                .id(this.id)
                .user(user)
                .post(post)
                .build();
    }

}
