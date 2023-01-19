package com.lion.pinepeople.service;


import com.lion.pinepeople.domain.dto.OrderResponse;
import com.lion.pinepeople.domain.dto.OrderSearchResponse;
import com.lion.pinepeople.domain.entity.Order;
import com.lion.pinepeople.domain.entity.OrderType;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.OrderRepository;
import com.lion.pinepeople.repository.PartyRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PartyRepository partyRepository;

    @Transactional
    public OrderResponse order(Long userId, Long partyId) {
        // 해당 id의 회원 없음
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, "존재하지 않는 회원입니다"));

        // 해당 id의 파티 없음
        Party findParty = partyRepository.findById(partyId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, "존재하지 않는 방입니다"));

        // 회원의 파티 존재여부 (에러 코드 추가 필요 - 존재하지 않는 파티)
        Optional<Order> orderParty = orderRepository.findByUserAndParty(findUser, findParty);

        if (OrderType.CREDIT_CARD == orderParty.get().getOrderType()) {
            orderParty.get().selectOrderType(OrderType.CREDIT_CARD);
        } else {
            orderParty.get().selectOrderType(OrderType.MEET_AND_PAY);
        }
        // 주문 생성
        Order createOrder = orderParty.get().creatOrder(findUser, findParty);

        // 주문 저장
        Order saveOrder = orderRepository.save(createOrder);

        return OrderResponse.builder()
                .message("주문 완료")
                .orderType(saveOrder.getOrderType())
                .orderDate(saveOrder.getOrderDate())
                .cost(saveOrder.getTotalCost())
                .discountPoint(saveOrder.getDiscountPoint())
                .build();
    }

    @Transactional(readOnly = true)
    public OrderSearchResponse findOrder(Long orderId, Long userId) {
        // 해당 id의 회원 없음
        userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, "존재하지 않는 회원입니다."));

        // 해당 id의 주문 없음
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, "해당 주문은 존재하지 않습니다."));
        return OrderSearchResponse.toDto(order);

    }

    @Transactional(readOnly = true)
    public Page<OrderSearchResponse> findAll(Long userId, Pageable pageable) {
        // 해당 id의 회원 없음
        userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, "존재하지 않는 회원입니다."));
        Page<Order> findAll = orderRepository.findAll(pageable);
        Page<OrderSearchResponse> findOrderAll = OrderSearchResponse.toDtoList(findAll);
        return findOrderAll;
    }
}
