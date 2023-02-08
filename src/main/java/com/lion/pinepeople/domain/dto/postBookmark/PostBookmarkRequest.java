package com.lion.pinepeople.domain.dto.postBookmark;


import com.lion.pinepeople.domain.entity.PostBookmark;

public class PostBookmarkRequest {


    private Long id;

    public PostBookmark of () {
        return PostBookmark.builder()
                .id(this.id)
                .build();
    }

}
