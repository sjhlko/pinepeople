package com.lion.pinepeople.domain.dto.party;

import com.lion.pinepeople.domain.dto.participant.ParticipantInfoResponse;
import com.lion.pinepeople.domain.entity.Participant;
import com.lion.pinepeople.domain.entity.Party;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PartyUpdateResponse {
    String message;
    PartyInfoResponse beforeEditing;
    PartyInfoResponse edited;


    public static PartyUpdateResponse of(Party beforeEditing, Party edited){
        return PartyUpdateResponse.builder()
                .message("파티 수정이 완료되었습니다.")
                .beforeEditing(PartyInfoResponse.of(beforeEditing))
                .edited(PartyInfoResponse.of(edited, beforeEditing.getCreatedAt()))
                .build();
    }

}
