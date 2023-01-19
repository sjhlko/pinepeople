package com.lion.pinepeople.domain.dto.party;

import com.lion.pinepeople.domain.entity.Party;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PartyDeleteResponse {
    String message;
    PartyInfoResponse deletedParty;

    public static PartyDeleteResponse of(Party deletedParty){
        return PartyDeleteResponse.builder()
                .message("파티 삭제가 완료되었습니다.")
                .deletedParty(PartyInfoResponse.of(deletedParty))
                .build();
    }
}
