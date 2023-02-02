package com.lion.pinepeople.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {

    COMMENT_ON_PARTY("회원님의 파티에 댓글이 달렸습니다."),
    COMMENT_ON_POST("회원님의 포스트에 댓글이 달렸습니다."),
    LIKE_ON_PARTY("회원님의 파티를 좋아합니다."),
    LIKE_ON_POST("회원님의 포스트를 좋아합니다."),
    REVIEW_ON_PARTY("회원님의 파티에 리뷰가 달렸습니다."),
    REJECT_JOIN_PARTY("파티 가입이 거절되었습니다."),
    APPROVE_JOIN_PARTY("파티 가입이 승인되었습니다."),
    APPLY_JOIN_PARTY("파티 가입 신청이 왔습니다.");

    private String message;
}
