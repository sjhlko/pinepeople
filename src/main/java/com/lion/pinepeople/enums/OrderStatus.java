package com.lion.pinepeople.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

    ORDER_COMPLETE("주문 완료"), // 주문 완료
    ORDER_CANCEL("주문 취소"); // 주문 취소

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
