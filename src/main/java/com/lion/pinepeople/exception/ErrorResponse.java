package com.lion.pinepeople.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private ErrorCode errorCode;
    private String message;

    public ErrorResponse(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
