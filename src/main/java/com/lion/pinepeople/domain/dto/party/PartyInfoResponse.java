package com.lion.pinepeople.domain.dto.party;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PartyInfoResponse {
    private String partyTitle;
    private String partyContent;
    private Integer partySize;
    private Integer partyCost;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd- hh:mm:ss", timezone = "Asia/Seoul")
    private Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd- hh:mm:ss", timezone = "Asia/Seoul")
    private Date endDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd- hh:mm:ss", timezone = "Asia/Seoul")
    private Timestamp createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd- hh:mm:ss", timezone = "Asia/Seoul")
    private Timestamp updatedAt;
    private String address;
    private String announcement;

    public static PartyInfoResponse of(Party party){
        return PartyInfoResponse.builder()
                .partyContent(party.getPartyContent())
                .address(party.getAddress())
                .partySize(party.getPartySize())
                .partyCost(party.getPartyCost())
                .partyTitle(party.getPartyTitle())
                .announcement(party.getAnnouncement())
                .createdAt(party.getCreatedAt())
                .updatedAt(party.getUpdatedAt())
                .startDate(party.getStartDate())
                .endDate(party.getEndDate())
                .build();
    }
}
