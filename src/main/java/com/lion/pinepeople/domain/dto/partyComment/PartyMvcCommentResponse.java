package com.lion.pinepeople.domain.dto.partyComment;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartyMvcCommentResponse {

    private Long partyId;
    private Long id;
    private String body;
}
