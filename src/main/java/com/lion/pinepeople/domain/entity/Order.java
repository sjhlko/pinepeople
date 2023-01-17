package com.lion.pinepeople.domain.entity;

import com.lion.pinepeople.domain.dto.OrderResponse;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "orders")
@Builder
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Enumerated(STRING)
    private OrderType orderType;

    private Integer cost;

    private Integer accumulateCost;

    private LocalDateTime orderDate;

    private Integer discountPoint;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

    // 주문 메소드
    public static Order creatOrder(User user, Party party) {
        Order order = new Order();
        return Order.builder()
                .user(user)
                .party(party)
                .orderDate(LocalDateTime.now())
                .orderType(order.getOrderType())
                .cost(order.getTotalCost())
                .accumulateCost(order.getAccumulateCost())
                .build();
    }

    // 총 주문 금액 메소드
    public int getTotalCost() {
        int totalCost = cost;
        totalCost -= getDiscountPoint();
        return totalCost;
    }

    // 주문 타입(카드 결제, 만나서 결제)
    public void selectOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

}
