package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
