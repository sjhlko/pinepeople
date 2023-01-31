package com.lion.pinepeople.mvc;

import com.lion.pinepeople.service.PartyCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PartyCommentMvcController {

    private final PartyCommentService partyCommentService;

    @PostMapping("/party/write-comment")
    public String doPartyComment(Authentication authentication) {
      log.info("파티 댓글 뷰단 :{}",authentication.getName());
        return "party/partyList";
    }
}
