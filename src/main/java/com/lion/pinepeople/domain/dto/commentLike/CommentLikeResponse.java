package com.lion.pinepeople.domain.dto.commentLike;


import com.lion.pinepeople.domain.entity.CommentLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentLikeResponse {


    private Long id;

//    public static LikeResponse of (Optional<CommentLike> clickedLike) {
//        return LikeResponse.builder()
//                .id(clickedLike.get().getLikedCommentId())
//                .build();
//    }

    public static CommentLikeResponse of (CommentLike clickedLike) {
        return CommentLikeResponse.builder()
                .id(clickedLike.getId())
                .build();
    }

}
