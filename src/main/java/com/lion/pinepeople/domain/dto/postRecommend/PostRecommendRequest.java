package com.lion.pinepeople.domain.dto.postRecommend;


import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.PostRecommend;
import com.lion.pinepeople.domain.entity.User;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class PostRecommendRequest {


    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;



    public PostRecommend of (User user, Post post) {
        return PostRecommend.builder()
                .id(this.id)
                .user(user)
                .post(post)
                .build();
    }

}
