package com.lion.pinepeople.service;



import com.lion.pinepeople.domain.entity.Order;

import com.lion.pinepeople.domain.dto.order.OrderInfoResponse;
import com.lion.pinepeople.domain.dto.order.OrderRequest;
import com.lion.pinepeople.domain.dto.order.OrderResponse;
import com.lion.pinepeople.domain.entity.Party;

import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.OrderRepository;
import com.lion.pinepeople.repository.PartyRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PartyRepository partyRepository;

    @Transactional
    public OrderResponse order(String userId, Long partyId, OrderRequest orderRequest) {
        // 해당 userId의 회원 없음
        User findUser = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        // 해당 partyId의 파티 없음
        Party findParty = partyRepository.findById(partyId)
                .orElseThrow(() -> new AppException(ErrorCode.PARTY_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        // 주문 생성
        Order createOrder = Order.createOrder(findUser,findParty,orderRequest.toEntity());

        // 총 결제 금액이 0원 미만이면 에러
        if (createOrder.getTotalCost(findParty) < 0) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "할인금액이 주문금액을 초과하였습니다. 다시 입력해주세요.");
        }

        /**
         * 회원테이블의 포인트에 누적 적립금 저장!?
         */
//        int totalPoints = findUser.getPoint() + createOrder.accumulatePoints(findParty);

        // 주문 저장
        Order saveOrder = orderRepository.save(createOrder);

        return OrderResponse.of(saveOrder);
    }

    @Transactional(readOnly = true)
    public OrderInfoResponse getOrder(String userId, Long orderId) {
        // 해당 userId의 회원 없음
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));

        // 해당 orderId의 주문 없음
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND, ErrorCode.ORDER_NOT_FOUND.getMessage()));

        if (order.getUser().getId() != user.getId()) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
        return OrderInfoResponse.toDto(order);

    }

    @Transactional(readOnly = true)
    public Page<OrderInfoResponse> getMyOrder(String userId, Pageable pageable) {
        // 해당 userId의 회원 없음
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));

        Page<Order> findAll = orderRepository.findOrdersByUser(user,pageable);
        Page<OrderInfoResponse> findOrderAll = OrderInfoResponse.toDtoList(findAll);
        return findOrderAll;
    }
}
