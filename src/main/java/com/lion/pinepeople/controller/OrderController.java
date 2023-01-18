package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.OrderRequest;
import com.lion.pinepeople.domain.dto.OrderResponse;
import com.lion.pinepeople.domain.dto.OrderSearchResponse;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderservice;

    /**
     * @param partyId 참가할 파티의 아이디
     * @param authentication 로그인한 아이디만 접근 가능
     * @return 주문 성공 메세지
     */
    @PostMapping("/party/{partyId}/orders")
    public Response<OrderResponse> order(@PathVariable Long partyId, Authentication authentication) {
        Long loginUserId = Long.parseLong(authentication.getName());
        OrderResponse order = orderservice.order(loginUserId, partyId);
        return Response.success(order);
    }

    /**
     * @param orderId 조회할 주문 아이디
     * @param authentication 로그인한 아이디만 접근 가능
     * @return 해당 주문 id의 상세 내역
     */
    @GetMapping("/users/order-lists/{orderId}")
    public Response<OrderSearchResponse> findOrder(@PathVariable Long orderId, Authentication authentication) {
        Long loginUserId = Long.parseLong(authentication.getName());
        OrderSearchResponse findOne = orderservice.findOrder(orderId, loginUserId);
        return Response.success(findOne);
    }

    /**
     *
     * @param pageable
     * @param authentication
     * @return 주문 내역 조회
     */
    @GetMapping("/users/order-lists")
    public Response<Page<OrderSearchResponse>> orderList(Pageable pageable, Authentication authentication) {
        Long loginUserId = Long.parseLong(authentication.getName());
        Page<OrderSearchResponse> findAll = orderservice.findAll(loginUserId, pageable);
        return Response.success(findAll);
    }
}
