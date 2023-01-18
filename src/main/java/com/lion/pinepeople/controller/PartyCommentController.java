package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.PartyCommentRequest;
import com.lion.pinepeople.domain.response.PartyCommentResponse;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.PartyCommentService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;


import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Api(tags = "파티 댓글")
@RestController
@RequiredArgsConstructor
public class PartyCommentController {

    private final PartyCommentService partyCommentService;

    @PostMapping("api/partys/{partyId}/party-comments")
    public Response addPartyComment(@PathVariable Long partyId, PartyCommentRequest partyCommentRequest, Authentication authentication) {
        long userId = Long.parseLong(authentication.getName());
        PartyCommentResponse response = partyCommentService.addPartyComment(partyId, userId, partyCommentRequest.getBody());
        return Response.success(response);
    }

 /*   @GetMapping("api/partys/{partyId}/party-comments")
    public Response getList(@PathVariable Long partyId, Pageable pageable) {


    }*/


}
