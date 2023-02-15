package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Order;

import com.lion.pinepeople.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findOrderByUserIdAndPartyIdAndOrderStatus(Long userId, Long partyId, OrderStatus orderStatus);
}
