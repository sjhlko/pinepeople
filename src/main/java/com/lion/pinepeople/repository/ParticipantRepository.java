package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Participant;
import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.enums.ParticipantRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Page<Participant> findAllByUserAndParticipantRole(Pageable pageable, User user, ParticipantRole participantRole);
}
