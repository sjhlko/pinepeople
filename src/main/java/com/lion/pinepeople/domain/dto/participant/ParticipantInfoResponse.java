package com.lion.pinepeople.domain.dto.participant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lion.pinepeople.domain.entity.Participant;
import com.lion.pinepeople.domain.entity.Party;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.enums.ApprovalStatus;
import com.lion.pinepeople.enums.ParticipantRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ParticipantInfoResponse {
    private Long id;
    private ApprovalStatus approvalStatus;
    private ParticipantRole participantRole;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd- hh:mm:ss", timezone = "Asia/Seoul")
    private Timestamp registeredAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd- hh:mm:ss", timezone = "Asia/Seoul")
    private Timestamp updatedAt;
    private Long userId;
    private Long partyId;
    public static ParticipantInfoResponse of(Participant participant){
        return ParticipantInfoResponse.builder()
                .id(participant.getId())
                .approvalStatus(participant.getApprovalStatus())
                .participantRole(participant.getParticipantRole())
                .registeredAt(participant.getRegisteredAt())
                .updatedAt(participant.getUpdatedAt())
                .partyId(participant.getParty().getId())
                .userId(participant.getUser().getId())
                .build();
    }

}
