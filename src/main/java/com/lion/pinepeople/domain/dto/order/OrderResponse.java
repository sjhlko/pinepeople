package com.lion.pinepeople.domain.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lion.pinepeople.domain.entity.Order;
import com.lion.pinepeople.domain.entity.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OrderResponse {

    private Integer cost;
    private OrderType orderType;
    private Integer discountPoint;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp orderDate;
    private String message;
    private Integer totalCost; // 총 결제 금액

    public static OrderResponse of(Order order) {
        return OrderResponse.builder()
                .message("주문 완료")
                .orderType(order.getOrderType())
                .orderDate(order.getOrderDate())
                .cost(order.getCost())
                .discountPoint(order.getDiscountPoint())
                .totalCost(order.totalCost(order.getParty()))
                .build();
    }
}
