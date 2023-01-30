package com.lion.pinepeople.exception;

import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.exception.customException.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> appExceptionHandler(AppException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(Response.error(new ErrorResponse(e.getErrorCode(), e.getMessage())));
    }

}
