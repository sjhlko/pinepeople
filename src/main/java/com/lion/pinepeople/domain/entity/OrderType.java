package com.lion.pinepeople.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderType {

    CREDIT_CARD("카드 결제"),
    MEET_AND_PAY("만나서 결제");

    private String message;


}
