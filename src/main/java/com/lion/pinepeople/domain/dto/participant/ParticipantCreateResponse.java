package com.lion.pinepeople.domain.dto.participant;

import com.lion.pinepeople.domain.dto.party.PartyCreateResponse;
import com.lion.pinepeople.domain.dto.party.PartyInfoResponse;
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
public class ParticipantCreateResponse {
    String message;
    ParticipantInfoResponse guest;
    public static ParticipantCreateResponse of(Participant participant){
        return ParticipantCreateResponse.builder()
                .message("파티원 신청이 완료되었습니다.")
                .guest(ParticipantInfoResponse.of(participant))
                .build();
    }

}
