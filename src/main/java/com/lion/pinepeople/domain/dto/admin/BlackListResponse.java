package com.lion.pinepeople.domain.dto.admin;


import com.lion.pinepeople.domain.entity.BlackList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@Slf4j
public class BlackListResponse {
    private Long blackListId;
    private LocalDateTime startDate;
    private List<String> fromUserEmail;
    private List<String> reportComment;

    public static BlackListResponse fromEntity(BlackList blackList, List<String> fromUserEmail, List<String> reportComment) {
        return BlackListResponse.builder()
                .blackListId(blackList.getBlackListId())
                .startDate(blackList.getStartDate())
                .fromUserEmail(fromUserEmail)
                .reportComment(reportComment)
                .build();
    }


}
