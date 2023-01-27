package com.lion.pinepeople.domain.dto.admin;

import com.lion.pinepeople.domain.entity.BlackList;
import com.lion.pinepeople.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class AllBlackListResponse {
    private Long blackListId;
    private User user;
    private LocalDateTime startDate;

    public static AllBlackListResponse fromEntity(BlackList blackList) {
        return AllBlackListResponse.builder()
                .blackListId(blackList.getBlackListId())
                .user(blackList.getUser())
                .startDate(blackList.getStartDate())
                .build();
    }

}
