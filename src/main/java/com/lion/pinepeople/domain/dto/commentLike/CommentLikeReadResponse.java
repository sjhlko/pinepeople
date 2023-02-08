package com.lion.pinepeople.domain.dto.commentLike;



import com.lion.pinepeople.domain.entity.CommentLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentLikeReadResponse {

    private Long id;



    public static CommentLikeResponse of(CommentLike clickedLike) {
        return CommentLikeResponse.builder()
                .id(clickedLike.getId())
                .build();
    }

    public static Page<CommentLikeReadResponse> of(Page<CommentLike> commentLikes) {

        return commentLikes.map(map -> CommentLikeReadResponse.builder()
                .id(map.getId())
                .build()
        );
    }
}
