package com.lion.pinepeople.service;



import com.lion.pinepeople.domain.dto.order.OrderCancelResponse;
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

    /**
     * 주문을 진행한다. 주문 시 사용한 포인트, 쌓인 적립금, 파티 인원수에 대한 정보를 업데이트한다.
     *
     * @param userId 주문하는 유저 아이디
     * @param partyId 참가할 파티 아이디
     * @param orderRequest 주문 정보 입력(결제수단, 포인트 사용액)
     * @return 주문완료
     */
    @Transactional
    public OrderResponse order(String userId, Long partyId, OrderRequest orderRequest) {
        User findUser = getUser(userId);
        Party findParty = getParty(partyId);

//        Integer integer = remainingSeats(findParty);
        Integer userOneCost = userOneCost(findParty);
        Integer accumulatePoint = getAccumulatePoint(orderRequest, findParty);
        Integer totalCost = totalCost(findUser, orderRequest, userOneCost);

        // 주문 생성
        Order createOrder = Order.createOrder(findUser,findParty,userOneCost,accumulatePoint,totalCost,orderRequest);

        // 주문 시 회원 포인트 정보 수정
        findUser.updatePoint(minusPoint(orderRequest, findUser, accumulatePoint));

        // 업데이트한 유저 저장
        userRepository.save(findUser);
        // 주문 저장
        Order saveOrder = orderRepository.save(createOrder);
        return OrderResponse.of(saveOrder);
    }

    /**
     * 주문 단건 조회
     *
     * @param userId 조회하는 유저 아이디
     * @param orderId 주문 아이디
     * @return 조회 완료
     */
    @Transactional(readOnly = true)
    public OrderInfoResponse getOrder(String userId, Long orderId) {
        User findUser = getUser(userId);
        Order order = getOrder(orderId);
        validateUser(findUser, order);
        return OrderInfoResponse.toDto(order);
    }

    /**
     * 나의 주문 전체 조회
     *
     * @param userId 조회하는 유저 아이디
     * @param pageable 전체 주문 내역 페이징 처리
     * @return 조회 완료
     */
    @Transactional(readOnly = true)
    public Page<OrderInfoResponse> getMyOrder(String userId, Pageable pageable) {
        User findUser = getUser(userId);
        Page<Order> findAll = orderRepository.findOrdersByUser(findUser,pageable);
        Page<OrderInfoResponse> findOrderAll = OrderInfoResponse.toDtoList(findAll);
        return findOrderAll;
    }

    /**
     * 주문을 취소한다. 취소 시 적립금과 회원의 포인트 정보 및 파티 인원수를 업데이트한다.
     *
     * @param userId 취소하는 유저 아이디
     * @param orderId 주문 아이디
     * @param partyId 참가한 파티 아이디
     * @return 주문 취소 완료
     */
    @Transactional
    public OrderCancelResponse cancelOrder(String userId, Long orderId, Long partyId) {
        User findUser = getUser(userId);
        getParty(partyId);
        Order findOrder = getOrder(orderId);
        validateUser(findUser, findOrder);

//        int remainingSeats = findParty.getPartySize() ++;

        // 주문 시 회원 포인트 정보 수정
        findUser.updatePoint(plusPoint(findUser, findOrder));

        // 주문 취소로 변경
        findOrder.cancelOrder(findOrder.getOrderStatus());

        // 업데이트한 유저 저장
        userRepository.save(findUser);
        // 주문 저장
        orderRepository.save(findOrder);
        return OrderCancelResponse.of(findOrder);
    }

    /** 해당 회원있는지 체크 **/
    private User getUser(String userId) {
        return userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
    }
     /** 해당 파티있는지 체크 **/
    private Party getParty(Long partyId) {
        return partyRepository.findById(partyId).orElseThrow(() -> new AppException(ErrorCode.PARTY_NOT_FOUND, ErrorCode.PARTY_NOT_FOUND.getMessage()));
    }
    /** 해당 주문있는지 체크 **/
    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND, ErrorCode.ORDER_NOT_FOUND.getMessage()));
    }
    /** 주문자와 로그인한 회원이 일치하는지 체크 **/
    private static void validateUser(User findUser, Order order) {
        if (order.getUser().getId() != findUser.getId()) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }


    /** 주문 시 회원 포인트 차감하는 메소드 (회원 기존 포인트 - 사용할 할인 금액 + 적립금) **/
    public Integer minusPoint(OrderRequest orderRequest, User findUser, Integer accumulatePoint) {
        return findUser.getPoint() - orderRequest.getDiscountPoint() + accumulatePoint;
    }

    /** 회원 한명의 파티 참가 비용 -> (파티 금액 / 파티 인원수) **/
    private Integer userOneCost(Party findParty) {
        return findParty.getPartyCost() / findParty.getPartySize();
    }

    /** 총 결제 비용 -> (회원 한명의 파티 참가 비용 - 할인금액)**/
    private Integer totalCost(User findUser, OrderRequest orderRequest, Integer userOneCost) {
        if (findUser.getPoint() < orderRequest.getDiscountPoint()) {
            throw new AppException(ErrorCode.INVALID_ORDER_TOTAL_COST,"현재 사용할 수 있는 포인트는 총 "+findUser.getPoint() + "포인트 입니다.");
        }
        int totalCost = userOneCost - orderRequest.getDiscountPoint();
        if (totalCost < 0) {
            throw new AppException(ErrorCode.INVALID_ORDER_TOTAL_COST,"총 결제금액인 0원 미만입니다. 할인금액을 다시 입력해주세요");
        }
        return totalCost;
    }

    /** 적립금 계산하는 메서드 -> ((회원 한명의 파티 참가 비용 - 할인금액) * 5% )**/
    private Integer getAccumulatePoint(OrderRequest orderRequest, Party findParty) {
        return (int) ((userOneCost(findParty) - orderRequest.getDiscountPoint()) * 0.05);
    }

    /** 주문 취소 시 회원의 포인트 복구하는 메서드 -> (회원 기존 포인트 + 사용포인트 - 적립금) **/
    public Integer plusPoint(User findUser, Order findOrder) {
        return findUser.getPoint() + findOrder.getDiscountPoint() - findOrder.getAccumulatePoint();
    }



    /**
     * 파티 인원 수 update해야 함
     */
//    // 주문 생성 시 파티 인원수 - 1
//    public Integer remainingSeats(Party findParty) {
//        int i = findParty.getPartySize() --;
//        if (i < 0) {
//            throw new AppException(ErrorCode.INVALID_PERMISSION,"이미 마감된 파티입니다.");
//        }
//        return i;
//    }

}
