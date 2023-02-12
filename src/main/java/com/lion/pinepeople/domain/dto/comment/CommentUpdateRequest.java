package com.lion.pinepeople.domain.dto.comment;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentUpdateRequest {



    private String comment;

    public void of() {
        this.comment = comment;
    }

}
