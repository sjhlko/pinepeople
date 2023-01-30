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
public class Report extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @Column(name = "from_user_id")
    private Long fromUserId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "report_content")
    private String reportContent;

    public static Report toEntity(Long loginUserId, User user, String content) {
        return Report.builder()
                .fromUserId(loginUserId)
                .user(user)
                .reportContent(content)
                .build();
    }
}
