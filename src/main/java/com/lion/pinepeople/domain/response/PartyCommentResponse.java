package com.lion.pinepeople.domain.response;

import com.lion.pinepeople.domain.entity.PartyComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

@Builder
@Getter
@AllArgsConstructor @NoArgsConstructor
public class PartyCommentResponse {

    private Long partyId;
    private Long partyCommentId;
    private String body;
    private String createdAt;


    public static PartyCommentResponse of(PartyComment partyComment) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        return PartyCommentResponse.builder()
                .partyId(partyComment.getParty().getId())
                .partyCommentId(partyComment.getId())
                .body(partyComment.getBody())
                .createdAt(sdf.format(partyComment.getCreatedAt()))
                .build();
    }


}
