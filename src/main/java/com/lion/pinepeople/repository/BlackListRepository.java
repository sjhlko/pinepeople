package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {
}
