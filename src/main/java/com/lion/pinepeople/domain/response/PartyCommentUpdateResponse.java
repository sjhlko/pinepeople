package com.lion.pinepeople.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PartyCommentUpdateResponse {

    public String message;
    public Long id;

    public static PartyCommentUpdateResponse of(Long id) {
        return PartyCommentUpdateResponse.builder()
                .message("파티 댓글이 수정 완료되었습니다")
                .id(id)
                .build();
    }
}
