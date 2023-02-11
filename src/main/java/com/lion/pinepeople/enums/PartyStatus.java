package com.lion.pinepeople.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum PartyStatus {
    RECRUITING("모집중"), CLOSED("마감됨");
    private String status;
}
