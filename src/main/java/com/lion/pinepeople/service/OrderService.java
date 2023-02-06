package com.lion.pinepeople.service;


import com.lion.pinepeople.domain.dto.order.*;
import com.lion.pinepeople.domain.entity.Order;
import com.lion.pinepeople.domain.entity.Party;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.enums.PaymentType;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.OrderRepository;
import com.lion.pinepeople.repository.PartyRepository;
import com.lion.pinepeople.repository.UserRepository;
import com.lion.pinepeople.repository.customRepository.OrderCustomRepository;
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
    private final OrderCustomRepository orderCustomRepository;

    /**
     * 주문을 진행한다. 주문 시 사용한 포인트, 쌓인 적립금, 파티 인원수에 대한 정보를 업데이트한다.
     *
     * @param userId       주문하는 유저 아이디
     * @param partyId      참가할 파티 아이디
     * @param orderRequest 주문 정보 입력(결제수단, 포인트 사용액)
     * @return 주문완료
     */
    @Transactional
    public OrderResponse order(String userId, Long partyId, OrderRequest orderRequest) {
        User findUser = getUser(userId);
        Party findParty = getParty(partyId);

        Integer userOneCost = userOneCost(findParty);

        // 총 결제 금액
        Integer totalCost = 0;
        if (!orderRequest.getPaymentType().equals(PaymentType.CONTACT_PAYMENT)) {
            totalCost = totalCost(findUser, orderRequest.getDiscountPoint(), userOneCost);
            Integer accumulatePoint = getAccumulatePoint(orderRequest.getDiscountPoint(), userOneCost);

            // 주문 생성
            Order createOrder = Order.createOrder(findUser, findParty, null, userOneCost, accumulatePoint, totalCost, orderRequest.getDiscountPoint(), orderRequest.getPaymentType());

            // 주문 시 회원 포인트 정보 수정
            findUser.updatePoint(minusPoint(orderRequest.getDiscountPoint(), findUser, accumulatePoint));

            // 업데이트한 유저 저장
            userRepository.save(findUser);
            // 주문 저장
            Order saveOrder = orderRepository.save(createOrder);
            return OrderResponse.of(saveOrder);
        } else {
            if (orderRequest.getDiscountPoint() > 0) {
                throw new AppException(ErrorCode.INVALID_ORDER_TOTAL_COST, "만나서 결제는 포인트를 사용할 수 없습니다.");
            }
            Order createOrder = Order.createOrder(findUser, findParty, null, userOneCost, 0, totalCost, orderRequest.getDiscountPoint(), orderRequest.getPaymentType());
            Order saveOrder = orderRepository.save(createOrder);
            return OrderResponse.of(saveOrder);
        }
    }

    /**
     * 주문 단건 조회
     *
     * @param userId  조회하는 유저 아이디
     * @param orderId 주문 아이디
     * @return 조회 완료
     */
    @Transactional(readOnly = true)
    public OrderInfoResponse getOrderDetail(String userId, Long orderId) {
        User findUser = getUser(userId);
        Order order = getOrder(orderId);
        validateUser(findUser, order);
        return OrderInfoResponse.toDto(order);
    }

    /**
     * 나의 주문 전체 조회
     *
     * @param userId   조회하는 유저 아이디
     * @param pageable 전체 주문 내역 페이징 처리
     * @return 조회 완료
     */
    @Transactional(readOnly = true)
    public Page<OrderInfoResponse> findMyOrder(String userId, OrderSearch orderSearch, Pageable pageable) {
        User findUser = getUser(userId);
        Page<Order> findOrderCon = orderCustomRepository.findAllByOrderStatus(orderSearch, findUser.getName(), pageable);
        return OrderInfoResponse.toDtoList(findOrderCon);
    }

    /**
     * 주문을 취소한다. 취소 시 적립금과 회원의 포인트 정보 및 파티 인원수를 업데이트한다.
     *
     * @param userId  취소하는 유저 아이디
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

        // 주문 시 회원 포인트 정보 수정
        findUser.updatePoint(plusPoint(findUser, findOrder));

        // 주문 취소로 변경
        findOrder.orderStatusChange(findOrder.getOrderStatus());

        // 업데이트한 유저 저장
        userRepository.save(findUser);
        // 주문 저장
        orderRepository.save(findOrder);
        return OrderCancelResponse.of(findOrder);
    }

    /**
     * 해당 회원있는지 체크
     **/
    public User getUser(String userId) {
        return userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
    }

    /**
     * 해당 파티있는지 체크
     **/
    public Party getParty(Long partyId) {
        return partyRepository.findById(partyId).orElseThrow(() -> new AppException(ErrorCode.PARTY_NOT_FOUND, ErrorCode.PARTY_NOT_FOUND.getMessage()));
    }

    /**
     * 해당 주문있는지 체크
     **/
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND, ErrorCode.ORDER_NOT_FOUND.getMessage()));
    }

    /**
     * 주문자와 로그인한 회원이 일치하는지 체크
     **/
    public static void validateUser(User findUser, Order order) {
        if (order.getUser().getId() != findUser.getId()) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }


    /**
     * 주문 시 회원 포인트 차감하는 메소드 (회원 기존 포인트 - 사용할 할인 금액 + 적립금)
     **/
    public Integer minusPoint(Integer discountPoint, User findUser, Integer accumulatePoint) {
        return findUser.getPoint() - discountPoint + accumulatePoint;
    }

    /**
     * 회원 한명의 파티 참가 비용 -> (파티 금액 / 파티 인원수)
     **/
    private Integer userOneCost(Party findParty) {
        if (findParty.getPartyCost() == 0) {
            return 0;
        }
        return findParty.getPartyCost() / findParty.getPartySize();
    }

    /**
     * 총 결제 비용 -> (회원 한명의 파티 참가 비용 - 할인금액)
     **/
    public Integer totalCost(User findUser, Integer discount, Integer userOneCost) {
        // 주문 시 받은 적립금을 다른 파티에 전부 사용 후, 주문 취소 시 회원의 포인트는 마이너스가 된다. 다음 주문 시 합산해서 결제하게 하기
        if (findUser.getPoint() < 0) {
            return (int) (userOneCost + (userOneCost * 0.1)) - findUser.getPoint();
        }
        if (findUser.getPoint() < discount) {
            throw new AppException(ErrorCode.INVALID_ORDER_POINT, "현재 사용할 수 있는 포인트는 총 " + findUser.getPoint() + "포인트 입니다.");
        }
        log.info("수수료 = {}", (int) (userOneCost * 0.1));

        int totalCost = (int) (userOneCost + (userOneCost * 0.1)) - discount;
        if (totalCost < 0) {
            throw new AppException(ErrorCode.INVALID_ORDER_TOTAL_COST, "총 결제금액인 0원 미만입니다. 할인금액을 다시 입력해주세요");
        }
        return totalCost;
    }


    /**
     * 적립금 계산하는 메서드 -> (총 결제 비용 * 5% )
     **/
    private Integer getAccumulatePoint(Integer discountPoint, Integer cost) {
        return (int) ((cost - discountPoint) * 0.05);
    }

    /**
     * 주문 취소 시 회원의 포인트 복구하는 메서드 -> (회원 기존 포인트 + 사용포인트 - 적립금)
     **/
    public Integer plusPoint(User findUser, Order findOrder) {
        int point = findUser.getPoint() + findOrder.getDiscountPoint() - findOrder.getAccumulatePoint();
        return point;
    }


    /**********************************MVC Service****************************************/

    @Transactional
    public OrderResponse orderMvc(String userId, Long partyId, OrderVo orderVo) {
        User findUser = getUser(userId);
        log.info("findUser={}", findUser);
        Party findParty = getParty(partyId);
        log.info("findParty={}", findParty);

        // 한명의 파티 참가 비용
        Integer userOneCost = userOneCost(findParty);
        log.info("userOneCost={}", userOneCost);

        //  총 결제 금액
        Integer totalCost = 0;

        // 카드 결제
        if (orderVo.getPaymentType().equals("CREDIT_CARD")) {

            // 총 결제 금액
            totalCost = totalCost(findUser, orderVo.getDiscountPoint(), userOneCost);
            log.info("총 결제 금액 ={} ", totalCost);

            // 적립금
            Integer accumulatePoint = getAccumulatePoint(orderVo.getDiscountPoint(), userOneCost);
            log.info("적립금 ={} ", accumulatePoint);

            // 주문 생성
            Order createOrder = Order.createOrder(findUser, findParty, orderVo.getImp_uid(), userOneCost, accumulatePoint, totalCost, orderVo.getDiscountPoint(), PaymentType.valueOf(orderVo.getPaymentType()));

            // 주문 시 회원 포인트 정보 수정
            Integer minusPoint = minusPoint(orderVo.getDiscountPoint(), findUser, accumulatePoint);
            log.info("주문 후 회원 포인트={}", minusPoint);

            // 업데이트한 유저 저장
            findUser.updatePoint(minusPoint);
            userRepository.save(findUser);

            // 주문 저장
            Order saveOrder = orderRepository.save(createOrder);
            return OrderResponse.of(saveOrder);
        } else {
            // 만나서 결제
            Order createOrder = Order.createOrder(findUser, findParty, orderVo.getImp_uid(), userOneCost, 0, totalCost, orderVo.getDiscountPoint(), PaymentType.valueOf(orderVo.getPaymentType()));
            Order saveOrder = orderRepository.save(createOrder);
            return OrderResponse.of(saveOrder);
        }
    }
}
