package com.lion.pinepeople.domain.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lion.pinepeople.domain.entity.Order;
import com.lion.pinepeople.enums.OrderStatus;
import com.lion.pinepeople.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;


@Getter
@AllArgsConstructor
@Builder
public class OrderInfoResponse {

    private Integer cost;
    private PaymentType paymentType;
    private Integer accumulatePoint;
    private Integer discountPoint;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp orderDate;
    private OrderStatus orderStatus;
    private Integer totalCost;

    /* Entity -> Dto 변환 */
    public static OrderInfoResponse toDto(Order order) {
        return OrderInfoResponse.builder()
                .orderDate(order.getOrderDate())
                .discountPoint(order.getDiscountPoint())
                .accumulatePoint(order.getAccumulatePoint())
                .cost(order.getCost())
                .paymentType(order.getPaymentType())
                .orderStatus(order.getOrderStatus())
                .totalCost(order.getTotalCost())
                .build();
    }

    /* Page<Entity> -> Page<Dto> 변환 */
    public static Page<OrderInfoResponse> toDtoList(Page<Order> orderList) {
        Page<OrderInfoResponse> orderDtoList = orderList.map(o -> OrderInfoResponse.builder()
                .orderDate(o.getOrderDate())
                .discountPoint(o.getDiscountPoint())
                .accumulatePoint(o.getAccumulatePoint())
                .cost(o.getCost())
                .paymentType(o.getPaymentType())
                .orderStatus(o.getOrderStatus())
                .totalCost(o.getTotalCost())
                .build());
        return orderDtoList;
    }


}
