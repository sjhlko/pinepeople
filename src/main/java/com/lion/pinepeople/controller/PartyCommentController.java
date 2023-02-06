package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.partyComment.*;
import com.lion.pinepeople.domain.response.*;
import com.lion.pinepeople.service.PartyCommentService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Api(tags = "PartyComment API")
@RestController
@RequiredArgsConstructor
@RequestMapping("pinepeople/api/partys/{partyId}/party-comments")
public class PartyCommentController {

    private final PartyCommentService partyCommentService;

    /**
     * 파티모집글에 댓글 쓰기
     *
     * @param partyId 파티 조회 목적
     * @param partyCommentRequest 파티 댓글 등록 dto
     * @return PartyCommentResponse 응답
     */
    @PostMapping
    public Response addPartyComment(@PathVariable Long partyId, @RequestBody PartyCommentRequest partyCommentRequest, Authentication authentication) {

        PartyCommentResponse response = partyCommentService.addPartyComment(partyId, authentication.getName(), partyCommentRequest.getBody());
        return Response.success(response);
    }

    /**
     * 파티모집글에 댓글 페이징 조회
     *
     * @param partyId 파티 조회 목적

     * @return Page<PartyCommentListResponse> 응답
     */
    @GetMapping
    public Response getList(@PathVariable Long partyId, @PageableDefault(size = 10,
            sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PartyCommentListResponse> responses =
                partyCommentService.getComments(partyId, pageable);
        return Response.success(responses);
    }

    /**
     * 파티모집글에 댓글 수정
     *
     * @param partyId 파티 조회 목적
     * @param commentId 파티 댓글 조회 목적
     * @param request 파티 댓글 수정 request
     * @return PartyCommentUpdateResponse 응답
     */
    @PatchMapping("/{commentId}")
    public Response updatePartyComment(@PathVariable Long partyId, @PathVariable Long commentId ,@RequestBody PartyCommentUpdateRequest request, Authentication authentication) {

        PartyCommentUpdateResponse response = partyCommentService.updateComment(partyId, commentId, request.getBody(), authentication.getName());
        return Response.success(response);
    }

    /**
     * 파티모집글에 댓글 삭제
     *
     * @param partyId 파티 조회 목적
     * @param commentId 파티 댓글 조회 목적
     * @return PartyCommentDeleteResponse 응답
     */
    @DeleteMapping("/{commentId}")
    public Response deletePartyComment(@PathVariable Long partyId, @PathVariable Long commentId , Authentication authentication) {

        PartyCommentDeleteResponse response = partyCommentService.deleteComment(partyId, commentId, authentication.getName());
        return Response.success(response);
    }


    @PostMapping("/{commentId}")
    public Response deletePostPartyComment(@PathVariable Long partyId, @PathVariable Long commentId , Authentication authentication) {
        PartyCommentDeleteResponse response = partyCommentService.deleteComment(partyId, commentId, authentication.getName());
        return Response.success(response);
    }


}
