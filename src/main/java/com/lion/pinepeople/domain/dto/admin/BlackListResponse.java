package com.lion.pinepeople.domain.dto.admin;


import com.lion.pinepeople.domain.entity.BlackList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class BlackListResponse {
    private Long blackListId;
    private LocalDateTime startDate;
    private List<String> fromUserEmail;

    public static BlackListResponse fromEntity(BlackList blackList, List<String> fromUserEmail) {
        return BlackListResponse.builder()
                .blackListId(blackList.getBlackListId())
                .startDate(blackList.getStartDate())
                .fromUserEmail(fromUserEmail)
                .build();
    }


}
