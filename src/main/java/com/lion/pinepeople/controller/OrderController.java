package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderservice;

//    @PostMapping("/party/{partyId}/orders")
//    public Response<OrderResponse> order(@PathVariable Long partyId, OrderRequest orderRequest) {
//
//        return Response.success();
//    }
}
