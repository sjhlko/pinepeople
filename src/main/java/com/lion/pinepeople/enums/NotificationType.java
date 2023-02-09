package com.lion.pinepeople.enums;

import lombok.Getter;

@Getter
public enum NotificationType {

    COMMENT_ON_PARTY("파티에 댓글을 남겼습니다."),
    COMMENT_ON_POST("포스트에 댓글을 남겼습니다."),
    LIKE_ON_PARTY("파티를 좋아합니다."),
    LIKE_ON_POST("포스트를 좋아합니다."),
    REVIEW_ON_PARTY("파티에 리뷰를 남겼습니다."),
    REJECT_JOIN_PARTY("파티 가입이 거절되었습니다."),
    APPROVE_JOIN_PARTY("파티 가입이 승인되었습니다."),
    APPLY_JOIN_PARTY("파티 가입 신청이 왔습니다.");

    private String message;

    NotificationType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}