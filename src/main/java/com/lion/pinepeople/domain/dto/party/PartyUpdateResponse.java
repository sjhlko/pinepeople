package com.lion.pinepeople.domain.dto.party;

import com.lion.pinepeople.domain.dto.participant.ParticipantInfoResponse;
import com.lion.pinepeople.domain.entity.Participant;
import com.lion.pinepeople.domain.entity.Party;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PartyUpdateResponse {
    String message;
    PartyInfoResponse edited;

    public static PartyUpdateResponse of(Timestamp createdAt, Party edited){
        return PartyUpdateResponse.builder()
                .message("파티 수정이 완료되었습니다.")
                .edited(PartyInfoResponse.of(edited, createdAt))
                .build();
    }

}
