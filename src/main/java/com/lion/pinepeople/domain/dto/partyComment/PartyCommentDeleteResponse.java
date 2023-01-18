package com.lion.pinepeople.domain.dto.partyComment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PartyCommentDeleteResponse {
    public String message;
    public Long id;

    public static PartyCommentDeleteResponse of(Long id) {
        return PartyCommentDeleteResponse.builder()
                .message("파티 댓글이 삭제 완료되었습니다")
                .id(id)
                .build();
    }
}
