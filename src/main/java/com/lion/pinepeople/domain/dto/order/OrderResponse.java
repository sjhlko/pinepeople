package com.lion.pinepeople.domain.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lion.pinepeople.domain.entity.Order;
import com.lion.pinepeople.enums.OrderStatus;
import com.lion.pinepeople.enums.PaymentType;
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
    private PaymentType paymentType;
    private OrderStatus orderStatus;
    private Integer discountPoint;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp orderDate;
    private String message;
    private Integer totalCost;

    public static OrderResponse of(Order order) {
        return OrderResponse.builder()
                .message("주문이 완료되었습니다.")
                .paymentType(order.getPaymentType())
                .orderStatus(order.getOrderStatus())
                .orderDate(order.getOrderDate())
                .cost(order.getCost())
                .discountPoint(order.getDiscountPoint())
                .totalCost(order.getTotalCost())
                .build();
    }
}
