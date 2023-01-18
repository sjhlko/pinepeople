package com.lion.pinepeople.domain.dto.partyComment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartyCommentUpdateRequest {
    public String body;
}
