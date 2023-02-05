package com.lion.pinepeople.domain.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lion.pinepeople.domain.entity.Order;
import com.lion.pinepeople.enums.OrderStatus;
import com.lion.pinepeople.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInfoResponse {

    private Long orderId;
    private Long partyId;
    private Integer cost;
    private String partyTitle;
    private PaymentType paymentType;
    private Integer accumulatePoint;
    private Integer discountPoint;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp orderDate;
    private OrderStatus orderStatus;
    private Integer totalCost;
    private String impUid;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp updatedAt;

    /* Entity -> Dto 변환 */
    public static OrderInfoResponse toDto(Order order) {
        return OrderInfoResponse.builder()
                .orderId(order.getId())
                .partyId(order.getParty().getId())
                .orderDate(order.getOrderDate())
                .partyTitle(order.getParty().getPartyTitle())
                .discountPoint(order.getDiscountPoint())
                .accumulatePoint(order.getAccumulatePoint())
                .cost(order.getCost())
                .paymentType(order.getPaymentType())
                .orderStatus(order.getOrderStatus())
                .totalCost(order.getTotalCost())
                .updatedAt(order.getUpdatedAt())
                .impUid(order.getImpUid())
                .build();
    }

    /* Page<Entity> -> Page<Dto> 변환 */
    public static Page<OrderInfoResponse> toDtoList(Page<Order> orderList) {
        Page<OrderInfoResponse> orderDtoList = orderList.map(o -> OrderInfoResponse.builder()
                .orderId(o.getId())
                .orderDate(o.getOrderDate())
                .partyTitle(o.getParty().getPartyTitle())
                .discountPoint(o.getDiscountPoint())
                .accumulatePoint(o.getAccumulatePoint())
                .cost(o.getCost())
                .paymentType(o.getPaymentType())
                .orderStatus(o.getOrderStatus())
                .totalCost(o.getTotalCost())
                .updatedAt(o.getUpdatedAt())
                .build());
        return orderDtoList;
    }


}
