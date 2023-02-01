package com.lion.pinepeople.domain.dto.comment;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CommentDeleteResponse {
    private String body;
    private Long id;

    public static CommentDeleteResponse ConvertToDto(String body, Long id) {
        return CommentDeleteResponse.builder()
                .body(body)
                .id(id)
                .build();
    }

}
