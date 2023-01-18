package com.lion.pinepeople.domain.dto;

import com.lion.pinepeople.domain.entity.Order;
import com.lion.pinepeople.domain.entity.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class OrderSearchResponse {

    private Integer cost;
    private OrderType orderType;
    private Integer accumulateCost;
    private Integer discountPoint;
    private LocalDateTime orderDate;

    /* Entity -> Dto 변환 */
    public static OrderSearchResponse toDto(Order order) {
        return OrderSearchResponse.builder()
                .orderDate(order.getOrderDate())
                .discountPoint(order.getDiscountPoint())
                .accumulateCost(order.getAccumulateCost())
                .cost(order.getTotalCost())
                .orderType(order.getOrderType())
                .build();
    }

    /* Page<Entity> -> Page<Dto> 변환 */
    public static Page<OrderSearchResponse> toDtoList(Page<Order> orderList) {

        Page<OrderSearchResponse> orderDtoList = orderList.map(o -> OrderSearchResponse.builder()
                .orderDate(o.getOrderDate())
                .discountPoint(o.getDiscountPoint())
                .accumulateCost(o.getAccumulateCost())
                .cost(o.getTotalCost())
                .orderType(o.getOrderType())
                .build());
        return orderDtoList;
    }



}
