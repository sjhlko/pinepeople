package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Long, Order> {
}
