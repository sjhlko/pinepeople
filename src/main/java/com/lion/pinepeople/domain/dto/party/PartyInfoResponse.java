package com.lion.pinepeople.domain.dto.party;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lion.pinepeople.domain.entity.Participant;
import com.lion.pinepeople.domain.entity.Party;
import com.lion.pinepeople.enums.PartyStatus;
import lombok.*;
import org.springframework.data.domain.Page;

import java.sql.Date;
import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class PartyInfoResponse {
    private Long partyId;
    private String partyTitle;
    private String partyContent;
    private Integer partySize;
    private Integer partyCost;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date endDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd- hh:mm:ss", timezone = "Asia/Seoul")
    private Timestamp createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd- hh:mm:ss", timezone = "Asia/Seoul")
    private Timestamp updatedAt;
    private String address;
    private String announcement;
    private String categoryName;
    private String hostName;
    private PartyStatus partyStatus;

    //파티 생성, 조회에 쓰이는 메소드
    public static PartyInfoResponse of(Party party){
        return getPartyInfoResponse(party);
    }

    //파티 수정시 createdAt이 null로 리턴되는 현상 해결을 위한 메소드
    public static PartyInfoResponse of(Party party, Timestamp createdAt){
        return PartyInfoResponse.builder()
                .partyId(party.getId())
                .partyContent(party.getPartyContent())
                .address(party.getAddress())
                .partySize(party.getPartySize())
                .partyCost(party.getPartyCost())
                .partyTitle(party.getPartyTitle())
                .announcement(party.getAnnouncement())
                .createdAt(createdAt)
                .hostName(party.getUser().getName())
                .updatedAt(party.getUpdatedAt())
                .startDate(party.getStartDate())
                .endDate(party.getEndDate())
                .partyStatus(party.getPartyStatus())
                .categoryName(party.getCategory().getName())
                .build();
    }

    //파티 참여자 테이블을 조회한 결과를 통해 속한 파티의 정보를 리턴
    public static PartyInfoResponse of(Participant participant){
        Party party = participant.getParty();
        return getPartyInfoResponse(party);
    }

    //파티 정보 리턴을 위한 of 메소드 사용시 중복되는 부분 분리
    private static PartyInfoResponse getPartyInfoResponse(Party party) {
        return PartyInfoResponse.builder()
                .partyId(party.getId())
                .partyContent(party.getPartyContent())
                .address(party.getAddress())
                .partySize(party.getPartySize())
                .partyCost(party.getPartyCost())
                .partyTitle(party.getPartyTitle())
                .announcement(party.getAnnouncement())
                .hostName(party.getUser().getName())
                .createdAt(party.getCreatedAt())
                .updatedAt(party.getUpdatedAt())
                .startDate(party.getStartDate())
                .endDate(party.getEndDate())
                .partyStatus(party.getPartyStatus())
                .categoryName(party.getCategory().getName())
                .build();
    }

    /* Page<Entity> -> Page<Dto> 변환처리 */
    public static Page<PartyInfoResponse> toPage(Page<Party> post){

        Page<PartyInfoResponse> pageResponse = post.map(m -> PartyInfoResponse.builder()
                .partyId(m.getId())
                .categoryName(m.getCategory().getName())
                .partyTitle(m.getPartyTitle())
                .partyContent(m.getPartyContent())
                .partySize(m.getPartySize())
                .partyCost(m.getPartyCost())
                .announcement(m.getAnnouncement())
                .address(m.getAddress())
                .createdAt(m.getCreatedAt())
                .updatedAt(m.getUpdatedAt())
                .startDate(m.getStartDate())
                .partyStatus(m.getPartyStatus())
                .endDate(m.getEndDate())
                .build());

        return pageResponse;
    }
}
