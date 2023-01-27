package com.lion.pinepeople.domain.dto.party;

import com.lion.pinepeople.domain.entity.Party;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PartyUpdateRequest {
    private String partyTitle;
    private String partyContent;
    private Integer partySize;
    private Integer partyCost;
    private Date startDate;
    private Date endDate;
    private String address;
    private String announcement;

    public Party toEntity(Party party){
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
                .category(party.getCategory())
                .user(party.getUser())
                .build();
    }
}
