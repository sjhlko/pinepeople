package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.order.OrderCancelResponse;
import com.lion.pinepeople.domain.dto.order.OrderInfoResponse;
import com.lion.pinepeople.domain.dto.order.OrderRequest;
import com.lion.pinepeople.domain.dto.order.OrderResponse;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Api(tags = "Order API")
public class OrderController {

    private final OrderService orderservice;

    /***
     * 주문을 진행한다.
     * @param partyId 참가할 파티 번호
     * @param orderRequest 주문 정보(결제 방법, 포인트 할인 금액)
     * @param authentication 로그인한 회원만 주문 가능
     * @return 주문 성공 메세지
     */
    @ApiOperation(value = "주문 생성")
    @PostMapping("/users/party/{partyId}/orders")
    public Response<OrderResponse> order(@PathVariable Long partyId, @RequestBody OrderRequest orderRequest, @ApiIgnore Authentication authentication) {
        log.info("controller");
        String userName = authentication.getName();
        OrderResponse order = orderservice.order(userName, partyId, orderRequest);
        return Response.success(order);
    }

    /**
     * 주문을 상세 조회한다. 주문한 파티 가격, 총 지불 금액, 주문 날짜 등의 정보를 조회할 수 있다.
     *
     * @param orderId        주문번호
     * @param authentication 로그인한 회원 본인의 주문만 접근 가능
     * @return 해당 주문번호의 주문 상세 내역
     */
    @ApiOperation(value = "주문 상세 조회")
    @GetMapping("/users/order-lists/{orderId}")
    public Response<OrderInfoResponse> getOrder(@PathVariable Long orderId, @ApiIgnore Authentication authentication) {
        String userName = authentication.getName();
        OrderInfoResponse findOne = orderservice.getOrderDetail(userName, orderId);
        return Response.success(findOne);
    }

    /**
     * 회원의 주문 내역을 모두 조회한다.
     *
     * @param pageable       주문 내역 페이징 처리
     * @param authentication 로그인한 회원 본인의 주문만 접근 가능
     * @return 해당 회원의 전체 주문 내역 조회
     */
    @ApiOperation(value = "나의 주문 내역")
    @GetMapping("/users/order-lists/my")
    public Response<Page<OrderInfoResponse>> myOrders(@PageableDefault(size = 10, sort = "orderDate", direction = Sort.Direction.DESC) Pageable pageable, @ApiIgnore Authentication authentication) {
        String userName = authentication.getName();
        Page<OrderInfoResponse> orderList = orderservice.getMyOrder(userName, pageable);
        return Response.success(orderList);
    }

    /**
     * 자신의 주문을 취소한다.
     *
     * @param orderId        주문번호
     * @param partyId        주문한 파티 번호
     * @param authentication 로그인한 회원 본인의 주문만 접근 가능
     * @return
     */
    @ApiOperation(value = "주문 취소")
    @PatchMapping("/users/{partyId}/orders/{orderId}")
    public Response<OrderCancelResponse> cancelOrder(@PathVariable Long orderId, @PathVariable Long partyId, @ApiIgnore Authentication authentication) {
        String userName = authentication.getName();
        OrderCancelResponse deleteOrder = orderservice.cancelOrder(userName, orderId, partyId);
        return Response.success(deleteOrder);
    }
}
