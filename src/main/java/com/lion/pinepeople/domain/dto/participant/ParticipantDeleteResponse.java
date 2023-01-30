package com.lion.pinepeople.domain.dto.participant;

import com.lion.pinepeople.domain.entity.Participant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ParticipantDeleteResponse {
    String message;
    ParticipantInfoResponse deleted;

    public static ParticipantDeleteResponse of(Participant deleted){
        return ParticipantDeleteResponse.builder()
                .message("파티 탈퇴가 완료되었습니다.")
                .deleted(ParticipantInfoResponse.of(deleted))
                .build();
    }
}
