package com.lion.pinepeople.domain.dto.comment;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CommentDeleteResponse {

    private Long id;

    public static CommentDeleteResponse of (Long id) {
        return CommentDeleteResponse.builder()
                .id(id)
                .build();
    }

}
