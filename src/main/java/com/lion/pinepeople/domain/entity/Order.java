package com.lion.pinepeople.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

import java.sql.Timestamp;
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
@Slf4j
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Enumerated(STRING)
    private OrderType orderType;

    private Integer cost; // 파티금액 / 파티원 = cost

    private Integer accumulateCost;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp orderDate;

    private Integer discountPoint; // cost >= discountPoint

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "party_id")
    private Party party;


    // 주문 메소드
    public static Order creatOrder(User user, Party party, Order order) {
        return Order.builder()
                .user(user)
                .party(party)
                .orderType(order.getOrderType())
                .orderDate(Timestamp.valueOf(LocalDateTime.now()))
                .cost(order.userOneCost(party))
                .discountPoint(order.discountPoint)
                .accumulateCost(order.accumulatePoints(party))
                .build();
    }

    // 적립금( 5% ? )
    public Integer accumulatePoints(Party party) {
        int points = (int) ((userOneCost(party) - discountPoint) * 0.05);
        return points;
    }

    // 회원 한명의 파티 참가 비용
    public static Integer userOneCost (Party party) {
        int price = party.getPartyCost() / party.getPartySize();
        return price;

    }

    /**
     * 총 결제 금액 컬럼 추가?!
     */
    public Integer totalCost(Party party) {
        int totalCost = userOneCost(party) - discountPoint;
        return totalCost;
    }

}
