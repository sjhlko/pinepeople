package com.lion.pinepeople.domain.dto.participant;

import com.lion.pinepeople.domain.entity.Participant;
import com.lion.pinepeople.domain.entity.Party;
import com.lion.pinepeople.enums.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ParticipantUpdateRequest {
    String approvalStatus;

    public Participant toEntity(Participant participant) {
        return Participant.builder()
                .party(participant.getParty())
                .participantRole(participant.getParticipantRole())
                .id(participant.getId())
                .user(participant.getUser())
                .approvalStatus(ApprovalStatus.valueOf(this.approvalStatus))
                .build();
    }
}
