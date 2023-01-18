package com.lion.pinepeople.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lion.pinepeople.enums.ApprovalStatus;
import com.lion.pinepeople.enums.ParticipantRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE participant SET deleted_at = current_timestamp WHERE participant_id = ? ")
public class Participant extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;
    @Enumerated(EnumType.STRING)
    private ParticipantRole participantRole;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    @JsonIgnore
    private Party party;

    public static Participant of(User user, Party party,ParticipantRole participantRole){
        return Participant.builder()
                .approvalStatus(
                        participantRole.equals(ParticipantRole.HOST)?
                                ApprovalStatus.APPROVED:ApprovalStatus.WAITING)
                .participantRole(participantRole)
                .user(user)
                .party(party)
                .build();
    }
}
