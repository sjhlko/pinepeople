package com.lion.pinepeople.domain.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lion.pinepeople.domain.entity.Order;
import com.lion.pinepeople.domain.entity.OrderType;
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
    private OrderType orderType;
    private Integer accumulateCost;
    private Integer discountPoint;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp orderDate;
    private Integer totalPrice; // 총 결제 금액

    /* Entity -> Dto 변환 */
    public static OrderInfoResponse toDto(Order order) {
        return OrderInfoResponse.builder()
                .orderDate(order.getOrderDate())
                .discountPoint(order.getDiscountPoint())
                .accumulateCost(order.getAccumulateCost())
                .cost(order.getCost())
                .orderType(order.getOrderType())
                .totalPrice(order.totalPrice(order.getParty()))
                .build();
    }

    /* Page<Entity> -> Page<Dto> 변환 */
    public static Page<OrderInfoResponse> toDtoList(Page<Order> orderList) {
        Page<OrderInfoResponse> orderDtoList = orderList.map(o -> OrderInfoResponse.builder()
                .orderDate(o.getOrderDate())
                .discountPoint(o.getDiscountPoint())
                .accumulateCost(o.getAccumulateCost())
                .cost(o.getCost())
                .orderType(o.getOrderType())
                .totalPrice(o.totalPrice(o.getParty()))
                .build());
        return orderDtoList;
    }



}
