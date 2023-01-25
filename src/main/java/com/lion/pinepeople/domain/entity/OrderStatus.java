package com.lion.pinepeople.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    ORDER_COMPLETE, // 주문 완료
    ORDER_CANCEL // 주문 취소

}
