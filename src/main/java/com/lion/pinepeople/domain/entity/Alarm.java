package com.lion.pinepeople.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Table(name = "alarms")
@Builder
@EntityListeners(AuditingEntityListener.class)
public class
Alarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @Enumerated(STRING)
    private AlarmType alarmType;

    private Long fromUserId;
    private Long targetId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Alarm fromParty(User user, Party party, AlarmType alarmType) {
        return Alarm.builder()
                .fromUserId(user.getId())
                .targetId(party.getId())
                .alarmType(alarmType)
                .user(party.getUser())
                .build();
    }

    public static Alarm fromPost(User user, Post post, AlarmType alarmType) {
        return Alarm.builder()
                .fromUserId(user.getId())
                .targetId(post.getId())
                .alarmType(alarmType)
                .user(post.getUser())
                .build();
    }

}
