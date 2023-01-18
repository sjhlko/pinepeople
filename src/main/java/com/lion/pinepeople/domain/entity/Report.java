package com.lion.pinepeople.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @Column(name = "from_user_id")
    private Long fromUserId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static Report toEntity(Long loginUserId, User user) {
        return Report.builder()
                .fromUserId(loginUserId)
                .user(user)
                .build();
    }
}
