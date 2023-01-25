package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.participant.ParticipantCreateResponse;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.ParticipantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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
}
