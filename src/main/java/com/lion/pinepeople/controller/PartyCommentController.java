package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.PartyCommentRequest;
import com.lion.pinepeople.domain.dto.PartyCommentUpdateRequest;
import com.lion.pinepeople.domain.response.*;
import com.lion.pinepeople.service.PartyCommentService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;


import org.springframework.data.domain.Page;
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

    @GetMapping("api/partys/{partyId}/party-comments")
    public Response getList(@PathVariable Long partyId, Pageable pageable) {
        Page<PartyCommentListResponse> responses =
                partyCommentService.getComments(partyId, pageable);
        return Response.success(responses);
    }

    @PatchMapping("api/partys/{partyId}/party-comments/{commentId}")
    public Response updatePartyComment(@PathVariable Long partyId, @PathVariable Long commentId , PartyCommentUpdateRequest request, Authentication authentication) {
        long userId = Long.parseLong(authentication.getName());
        PartyCommentUpdateResponse response = partyCommentService.updateComment(partyId, commentId, request.getBody(), userId);
        return Response.success(response);
    }

    @DeleteMapping("api/partys/{partyId}/party-comments/{commentId}")
    public Response deletePartyComment(@PathVariable Long partyId, @PathVariable Long commentId , Authentication authentication) {
        long userId = Long.parseLong(authentication.getName());
        PartyCommentDeleteResponse response = partyCommentService.deleteComment(partyId, commentId, userId);
        return Response.success(response);
    }


}
