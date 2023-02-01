package com.lion.pinepeople.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "UserName이 중복됩니다."),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "Email이 중복됩니다."),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 user를 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저는 존재하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "패스워드가 잘못되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "사용자가 권한이 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 포스트가 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "답글이 없습니다."),
    ALREADY_LIKED(HttpStatus.FORBIDDEN, "이미 좋아요를 눌렀습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카테고리를 찾을 수 없습니다."),
    CATEGORY_NAME_FALUT(HttpStatus.CONFLICT, "카테고리의 branch와 name이 같을 수 없습니다. "),
    BRIX_NOT_FOUND(HttpStatus.CONFLICT, "해당 당도를 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문이 없습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB에러"),
    USER_ROLE_NOT_FOUND(HttpStatus.NOT_ACCEPTABLE, "해당 UserRole은 존재하지 않습니다."),
    PARTY_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 파티 댓글이 없습니다."),
    PARTY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 파티가 없습니다."),
    DUPLICATED_REPORT(HttpStatus.CONFLICT, "중복된 신고입니다."),
    EXPIRE_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "로그인을 다시 해주세요."),
    BLACKLIST_NOT_FOUND(HttpStatus.NOT_FOUND, "블랙리스트에서 해당 내용을 찾을 수 없습니다."),
    DUPLICATED_PARTICIPANT(HttpStatus.CONFLICT, "해당 파티에 이미 속해있거나, 가입신청되었습니다."),
    PARTICIPANT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 파티원이 없습니다."),
    EXPIRE_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_ORDER_POINT(HttpStatus.BAD_REQUEST, "잘못된 포인트 사용입니다."),
    INVALID_ORDER_TOTAL_COST(HttpStatus.BAD_REQUEST, "잘못된 결제 금액입니다."),
    INVALID_ORDER(HttpStatus.BAD_REQUEST, "잘못된 주문입니다.");

    private HttpStatus status;
    private String message;
}
