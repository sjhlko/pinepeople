package com.lion.pinepeople.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "UserName이 중복됩니다."),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 user를 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저는 존재하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "패스워드가 잘못되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "사용자가 권한이 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 포스트가 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "답글이 없습니다."),
    ALREADY_LIKED(HttpStatus.FORBIDDEN, "이미 좋아요를 눌렀습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB에러"),
    BRIX_NOT_FOUND(HttpStatus.NOT_ACCEPTABLE, "해당 User의 당도를 찾을 수 없습니다."),
    USER_ROLE_NOT_FOUND(HttpStatus.NOT_ACCEPTABLE, "해당 UserRole은 존재하지 않습니다.");

    private HttpStatus status;
    private String message;
}
