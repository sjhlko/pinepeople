package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Participant;
import com.lion.pinepeople.domain.entity.Party;
import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.enums.ApprovalStatus;
import com.lion.pinepeople.enums.ParticipantRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Page<Participant> findAllByUserAndParticipantRoleAndApprovalStatus(Pageable pageable, User user, ParticipantRole participantRole, ApprovalStatus approvalStatus);
    Page<Participant> findAllByPartyAndApprovalStatus(Pageable pageable, Party party, ApprovalStatus approvalStatus);
    Optional<Participant> findParticipantByUserAndParty(User user, Party party);
    @Query(value = "SELECT COUNT(*) FROM participant p where p.approval_status = :approval_status and p.party_id = :party_id", nativeQuery = true)
    Long countByApprovalStatus(@Param("approval_status") String approvalStatus, @Param("party_id") Long partyId);

}
