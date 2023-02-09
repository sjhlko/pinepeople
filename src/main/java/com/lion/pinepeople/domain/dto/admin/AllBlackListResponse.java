package com.lion.pinepeople.domain.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startDate;

    public static AllBlackListResponse fromEntity(BlackList blackList) {
        return AllBlackListResponse.builder()
                .blackListId(blackList.getBlackListId())
                .user(blackList.getUser())
                .startDate(blackList.getStartDate())
                .build();
    }

}
