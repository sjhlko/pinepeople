package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Party;
import com.lion.pinepeople.domain.entity.PartyComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartyCommentRepository extends JpaRepository<PartyComment , Long> {

    Page<PartyComment> findAllByParty(Party party, Pageable pageable);
    List<PartyComment> findListByParty(Party party);
}
