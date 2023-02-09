package com.lion.pinepeople.domain.entity;

import com.lion.pinepeople.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Notification")
@EntityListeners(AuditingEntityListener.class)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Embedded
    private NotificationContent content;

    @Enumerated(STRING)
    private NotificationType notificationType;

    @Embedded
    private RelatedURL url; // 클릭 시 이동될 링크

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User receiver; // 알람을 받은 유저

    private Boolean isRead; // 알람 읽음 처리

    @Builder
    public Notification(Long id, String content, NotificationType notificationType, String url, User receiver, Boolean isRead) {
        this.id = id;
        this.content = new NotificationContent(content);
        this.notificationType = notificationType;
        this.url = new RelatedURL(url);
        this.receiver = receiver;
        this.isRead = isRead;
    }

    public String getUrl() {
        return url.getUrl();
    }

    public String getContent() {
        return content.getContent();
    }

    public void read() {
        this.isRead = true;
    }
}

