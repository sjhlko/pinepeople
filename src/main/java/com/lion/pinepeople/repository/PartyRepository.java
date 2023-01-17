package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {
}
