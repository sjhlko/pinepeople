package com.lion.pinepeople.domain.dto.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lion.pinepeople.domain.entity.Notification;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
public class NotificationDto {

    private Long id;
    private String content;
    private String url;
    private String notificationType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd- hh:mm:ss", timezone = "Asia/Seoul")
    private Timestamp createdAt;
    private Boolean isRead;

    @Builder
    public NotificationDto(Long id, String content, String url, String notificationType, Timestamp createdAt, Boolean isRead) {
        this.id = id;
        this.content = content;
        this.url = url;
        this.notificationType = notificationType;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    public static NotificationDto from(Notification notification) {
        return NotificationDto.builder()
                .notificationType(notification.getNotificationType().getMessage())
                .isRead(notification.getIsRead())
                .id(notification.getId())
                .content(notification.getContent())
                .createdAt(notification.getCreatedAt())
                .url(notification.getUrl())
                .build();
    }

    public static Page<NotificationDto> toDtoList(Page<Notification> notificationList) {
        Page<NotificationDto> dtoPage = notificationList.map(notify -> NotificationDto.builder()
                .id(notify.getId())
                .isRead(notify.getIsRead())
                .content(notify.getContent())
                .url(notify.getUrl())
                .notificationType(notify.getNotificationType().getMessage())
                .createdAt(notify.getCreatedAt())
                .build());
        return dtoPage;
    }

}