package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.party.PartyCreateRequest;
import com.lion.pinepeople.domain.dto.party.PartyCreateResponse;
import com.lion.pinepeople.domain.dto.user.role.UserRoleResponse;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.PartyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Party API")
@RestController
@RequestMapping("/api/partys")
@RequiredArgsConstructor
public class PartyController {
    private final PartyService partyService;
    @PostMapping("")
    @ApiOperation(value = "파티 생성")
    public Response<PartyCreateResponse> createParty(@RequestBody PartyCreateRequest partyCreateRequest, Authentication authentication) {
        PartyCreateResponse partyCreateResponse = partyService.createParty(partyCreateRequest, authentication.getName());
        return Response.success(partyCreateResponse);
    }
}
