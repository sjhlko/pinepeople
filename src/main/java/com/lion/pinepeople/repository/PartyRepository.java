package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Party;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.repository.customRepository.PartyRepositoryCustom;
import net.bytebuddy.asm.Advice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface PartyRepository extends JpaRepository<Party, Long>, PartyRepositoryCustom {
    Page<Party> findAllByUser(Pageable pageable, User user);

    @Transactional(readOnly = true)
    Page<Party> findByCategory_Name(Pageable pageable, String name);
}
