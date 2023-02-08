package com.lion.pinepeople.domain.dto.commentLike;

import com.lion.pinepeople.domain.entity.CommentLike;


public class
CommentLikeRequest {

    private Long id;

    public CommentLike of () {
        return CommentLike.builder()
                .id(this.id)
                .build();
    }

}
