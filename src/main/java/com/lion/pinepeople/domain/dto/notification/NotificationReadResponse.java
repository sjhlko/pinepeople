package com.lion.pinepeople.domain.dto.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lion.pinepeople.domain.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
public class NotificationReadResponse {

    private Long notificationId;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd- hh:mm:ss", timezone = "Asia/Seoul")
    private Timestamp createdAt;
    private Boolean isRead;

    @Builder
    public NotificationReadResponse(Long notificationId, String content, Timestamp createdAt, Boolean isRead) {
        this.notificationId = notificationId;
        this.content = content;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    public static NotificationReadResponse from(Notification notification) {
        return NotificationReadResponse.builder()
                .notificationId(notification.getId())
                .content(notification.getContent())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
