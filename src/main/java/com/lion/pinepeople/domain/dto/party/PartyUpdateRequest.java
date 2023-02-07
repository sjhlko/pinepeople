package com.lion.pinepeople.domain.dto.party;

import com.lion.pinepeople.domain.entity.Category;
import com.lion.pinepeople.domain.entity.Party;
import lombok.*;

import java.sql.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PartyUpdateRequest {
    private String partyTitle;
    private String partyContent;
    private Integer partySize;
    private Integer partyCost;
    private Date startDate;
    private Date endDate;
    private String address;
    private String announcement;
    private String branch; // 카테고리 대분류
    private String code; // 카테고리 소분류

    public Party toEntity(Party party, Category category){
        return Party.builder()
                .id(party.getId())
                .partyContent(this.partyContent)
                .address(this.address)
                .partySize(this.partySize)
                .partyCost(this.partyCost)
                .partyTitle(this.partyTitle)
                .announcement(this.announcement)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .participants(party.getParticipants())
                .category(category)
                .partyStatus(party.getPartyStatus())
                .user(party.getUser())
                .build();
    }

    public static PartyUpdateRequest of(PartyUpdateRequest partyUpdateRequest, String branch, String code){
        return PartyUpdateRequest.builder()
                .partyContent(partyUpdateRequest.getPartyContent())
                .address(partyUpdateRequest.getAddress())
                .partySize(partyUpdateRequest.getPartySize())
                .partyCost(partyUpdateRequest.getPartyCost())
                .partyTitle(partyUpdateRequest.getPartyTitle())
                .announcement(partyUpdateRequest.getAnnouncement())
                .startDate(partyUpdateRequest.getStartDate())
                .endDate(partyUpdateRequest.getEndDate())
                .branch(branch)
                .code(code)
                .build();
    }
}
