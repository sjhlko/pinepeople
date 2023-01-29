package com.lion.pinepeople.domain.dto.participant;

import com.lion.pinepeople.domain.dto.party.PartyInfoResponse;
import com.lion.pinepeople.domain.dto.party.PartyUpdateResponse;
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
public class ParticipantUpdateResponse {
    String message;
    ParticipantInfoResponse edited;

    public static ParticipantUpdateResponse of(Timestamp createdAt, Participant edited){
        return ParticipantUpdateResponse.builder()
                .message("파티원 정보 수정이 완료되었습니다.")
                .edited(ParticipantInfoResponse.of(edited, createdAt))
                .build();
    }
}
