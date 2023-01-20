package com.lion.pinepeople.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lion.pinepeople.domain.dto.order.OrderRequest;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value={"updatedAt"}, allowGetters=true)
public class Order{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private Integer cost;

    private Integer accumulateCost;

    private Integer discountPoint;

    private Integer totalCost;

    // 주문 상태(주문 취소, 주문 완료)
    @Enumerated(STRING)
    private OrderStatus orderStatus;

    // 결제 수단(카드 결제, 만나서 결제)
    @Enumerated(STRING)
    private PaymentType paymentType;

    @CreatedDate
    @Column(name = "order_date", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp orderDate;

    @LastModifiedDate
    @Column(name = "update_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp updatedAt;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "party_id")
    private Party party;


    /** 주문 생성 메소드 **/
    public static Order createOrder(User user, Party party, Integer userOneCost, Integer accumulatePoint,Integer totalCost, OrderRequest orderRequest) {
        return Order.builder()
                .user(user)
                .party(party)
                .orderStatus(OrderStatus.ORDER_COMPLETE)
                .paymentType(orderRequest.getPaymentType())
                .discountPoint(orderRequest.getDiscountPoint())
                .orderDate(Timestamp.valueOf(LocalDateTime.now()))
                .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                .cost(userOneCost)
                .accumulateCost(accumulatePoint)
                .totalCost(totalCost)
                .build();
    }

    /** 주문 취소 시 주문 상태 변경하는 메소드 **/
    public void cancelOrder(OrderStatus orderStatus) {
        if (orderStatus.equals(OrderStatus.ORDER_CANCEL)) {
            throw new AppException(ErrorCode.DATABASE_ERROR, "이미 취소된 주문입니다.");
        }
        this.orderStatus = OrderStatus.ORDER_CANCEL;
    }
}
