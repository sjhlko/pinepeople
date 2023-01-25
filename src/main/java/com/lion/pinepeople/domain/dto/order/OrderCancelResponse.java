package com.lion.pinepeople.domain.dto.order;

import com.lion.pinepeople.domain.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCancelResponse {

    private String message;
    private Long orderId;

    public static OrderCancelResponse of(Order order) {
        return OrderCancelResponse.builder()
                .orderId(order.getId())
                .message("주문이 취소되었습니다.")
                .build();
    }
}
