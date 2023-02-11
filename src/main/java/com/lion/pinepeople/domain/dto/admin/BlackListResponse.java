package com.lion.pinepeople.domain.dto.admin;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.lion.pinepeople.domain.entity.BlackList;
import com.lion.pinepeople.domain.entity.User;
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
    private User user;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startDate;
    private List<String> fromUserEmail;
    private List<String> reportComment;

    public static BlackListResponse fromEntity(BlackList blackList, List<String> fromUserEmail, List<String> reportComment) {
        return BlackListResponse.builder()
                .blackListId(blackList.getBlackListId())
                .user(blackList.getUser())
                .startDate(blackList.getStartDate())
                .fromUserEmail(fromUserEmail)
                .reportComment(reportComment)
                .build();
    }


}
