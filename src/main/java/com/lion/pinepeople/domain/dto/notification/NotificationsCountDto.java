package com.lion.pinepeople.domain.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationsCountDto {

    /**
     * 로그인 한 유저가 읽지 않은 알림 수
     */
    private long unReadCount;
}
