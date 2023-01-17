package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.PartyCommentService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "파티원 모집 댓글")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account_books")
public class PartyCommentController {

    private final PartyCommentService partyCommentService;
//
//    @PostMapping("api/party/{partyId}/party-comment")
//    public Response addPartyComment(@PathVariable Long partyId, Authentication authentication) {
//
//    }


}
