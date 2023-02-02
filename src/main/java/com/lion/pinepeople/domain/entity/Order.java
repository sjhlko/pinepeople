package com.lion.pinepeople.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@JsonIgnoreProperties(value = {"updatedAt"}, allowGetters = true)
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private Integer cost;

    private Integer accumulatePoint;

    private Integer discountPoint;

    private Integer totalCost;

    // 주문 상태(주문 취소, 주문 완료)
    @Enumerated(STRING)
    private OrderStatus orderStatus;

    // 결제 수단(카드 결제, 만나서 결제)
    @Enumerated(STRING)
    private PaymentType paymentType;

    private String impUid; // 아임포트 결제 번호
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


    /**
     * MVC 주문 생성 메소드
     **/
    public static Order createOrder(User user, Party party, String impUid, Integer userOneCost, Integer accumulatePoint, Integer totalCost, Integer discountPoint, PaymentType paymentType) {
        return Order.builder()
                .user(user)
                .party(party)
                .impUid(impUid)
                .orderStatus(OrderStatus.ORDER_COMPLETE)
                .paymentType(paymentType)
                .discountPoint(discountPoint)
                .orderDate(Timestamp.valueOf(LocalDateTime.now()))
                .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                .cost(userOneCost)
                .accumulatePoint(accumulatePoint)
                .totalCost(totalCost)
                .build();
    }

    /**
     * 주문 취소 시 주문 상태 변경하는 메소드
     **/
    public void orderStatusChange(OrderStatus orderStatus) {
        if (orderStatus.equals(OrderStatus.ORDER_CANCEL)) {
            throw new AppException(ErrorCode.INVALID_ORDER, "이미 취소된 주문입니다.");
        }
        this.orderStatus = OrderStatus.ORDER_CANCEL;
    }
}
