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
public class PartyCreateResponse {
    String message;
    PartyInfoResponse party;
    ParticipantInfoResponse host;
    public static PartyCreateResponse of(Party party, Participant participant){
        return PartyCreateResponse.builder()
                .message("파티 생성이 완료되었습니다.")
                .party(PartyInfoResponse.of(party))
                .host(ParticipantInfoResponse.of(participant))
                .build();
    }
}
