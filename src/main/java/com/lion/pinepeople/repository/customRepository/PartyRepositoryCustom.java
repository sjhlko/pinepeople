package com.lion.pinepeople.repository.customRepository;

import com.lion.pinepeople.domain.entity.Party;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PartyRepositoryCustom {
    Page<Party> findBySearchOption(Pageable pageable, String address, String content, String title);
}
