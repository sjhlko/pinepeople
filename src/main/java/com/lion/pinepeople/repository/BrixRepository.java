package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Brix;
import com.lion.pinepeople.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrixRepository extends JpaRepository<Brix, Long> {
    Optional<Brix> findByUser(User user);
}
