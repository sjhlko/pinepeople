package com.lion.pinepeople.domain.entity;

import com.lion.pinepeople.enums.BlackListStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class BlackList extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blackList_id")
    private Long blackListId;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private BlackListStatus status;

    public static BlackList toEntity(LocalDateTime dateTime, User user, BlackListStatus status){
        return BlackList.builder()
                .startDate(dateTime)
                .user(user)
                .status(status)
                .build();
    }

    public void updateStatus(BlackListStatus approval) {
        this.status = approval;
    }
}
