package com.lion.pinepeople.domain.dto.postBookmark;

import com.lion.pinepeople.domain.entity.PostBookmark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostBookmarkReadResponse {


    private Long id;

    public static PostBookmarkReadResponse of(PostBookmark bookmarkedPost) {
        return PostBookmarkReadResponse.builder()
                .id(bookmarkedPost.getId())
                .build();
    }


    public static Page<PostBookmarkReadResponse> of(Page<PostBookmark> postBookmarks) {

        return postBookmarks.map(map -> PostBookmarkReadResponse.builder()
                .id(map.getId())
                .build()
        );
    }
}