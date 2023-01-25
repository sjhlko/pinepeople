package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.participant.ParticipantCreateResponse;
import com.lion.pinepeople.domain.dto.participant.ParticipantInfoResponse;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.enums.ApprovalStatus;
import com.lion.pinepeople.service.ParticipantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Participant API")
@RestController
@RequestMapping("/api/partys/{partyId}/participants")
@RequiredArgsConstructor
public class ParticipantController {
    private final ParticipantService participantService;

    /**
     * 파티원 생성(파티원 신청)
     * 파티원 신청시 guest로 waiting 상태로 등록되게 된다.
     * @return 생성된 파티의 상세 정보와 생성한 host 의 상세정보
     * **/
    @PostMapping
    @ApiOperation(value = "파티원 생성")
    public Response<ParticipantCreateResponse> createParticipant(@PathVariable Long partyId, Authentication authentication) {
        ParticipantCreateResponse participantCreateResponse = participantService.createGuestParticipant(partyId,authentication.getName());
        return Response.success(participantCreateResponse);
    }

    /**
     * 특정 파티의 approved 상태의 파티원을 모두 조회한다.
     * @return 해당 파티의 approved 상태의 approvalStatus 를 가진 파티원들의 정보가 페이징되어 리턴됨
     */
    @GetMapping("")
    @ApiOperation(value = "파티원 전체 조회")
    public Response<Page<ParticipantInfoResponse>> getAllParticipant(@PageableDefault(size = 20, sort ="createdAt",
            direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Long partyId) {
        Page<ParticipantInfoResponse> participantInfoResponses = participantService.getApprovedParticipant(pageable,partyId);
        return Response.success(participantInfoResponses);
    }

    /**
     * 특정 파티의 Waiting 상태의 파티원을 모두 조회한다. 해당 파티의 파티장인 경우에만 가능하다
     * @return 해당 파티의 waiting 상태의 approvalStatus 를 가진 파티원들의 정보가 페이징되어 리턴됨
     */
    @GetMapping("/waits")
    @ApiOperation(value = "승인 대기중인 파티원 조회")
    public Response<Page<ParticipantInfoResponse>> getWaitingParticipant(@PageableDefault(size = 20, sort ="createdAt",
            direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Long partyId, Authentication authentication) {
        Page<ParticipantInfoResponse> participantInfoResponses = participantService.getWaitingParticipant(pageable,partyId,authentication.getName());
        return Response.success(participantInfoResponses);
    }
}
