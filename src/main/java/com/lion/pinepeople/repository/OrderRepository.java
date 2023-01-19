package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Order;
import com.lion.pinepeople.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByUserAndParty(User user, Party party);
    Page<Order> findAll(Pageable pageable);
}
