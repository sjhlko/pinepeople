package com.lion.pinepeople.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lion.pinepeople.domain.dto.order.OrderRequest;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
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
@Slf4j
@Builder
@AllArgsConstructor
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

    private Integer totalCost;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

    // 주문 생성 메소드
    public static Order createOrder(User user, Party party, OrderRequest orderRequest) {
        return Order.builder()
                .user(user)
                .party(party)
                .orderType(orderRequest.getOrderType())
                .orderDate(Timestamp.valueOf(LocalDateTime.now()))
                .cost(Order.userOneCost(party))
                .discountPoint(orderRequest.getDiscountPoint())
                .accumulateCost(Order.getAccumulatePoint(party,orderRequest))
                .totalCost(Order.getTotalCost(party,orderRequest))
                .build();
    }


    // 적립금( 5% ? )
    public static Integer getAccumulatePoint(Party party, OrderRequest orderRequest) {
        int points = (int) ((userOneCost(party) - orderRequest.getDiscountPoint()) * 0.05);
//        Integer point = user.getPoint();
//        point += points;
        return points;
    }

    // 회원 한명의 파티 참가 비용
    public static Integer userOneCost (Party party) {
        int price = party.getPartyCost() / party.getPartySize();
        return price;

    }

    // 총 결제 비용(회원 한명의 파티 참가 비용 - 할인 금액)
    public static Integer getTotalCost(Party party, OrderRequest orderRequest) {
        int totalCost = userOneCost(party) - orderRequest.getDiscountPoint();
        // 총 결제 금액이 0원 미만이면 에러
        if (totalCost < 0) {
            throw new AppException(ErrorCode.INVALID_PERMISSION,"총 결제금액인 0원 미만입니다. 할인금액을 다시 입력해주세요");
        }
        return totalCost;
    }

}
