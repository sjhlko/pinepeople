package com.lion.pinepeople.mvc;

import com.lion.pinepeople.domain.dto.partyComment.PartyCommentResponse;
import com.lion.pinepeople.domain.dto.partyComment.PartyMvcCommentResponse;
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

    @PostMapping("/users/party/write-comment")
    public String doPartyComment(Authentication authentication, PartyMvcCommentResponse response) {
      log.info("파티 댓글 뷰단 :{}",authentication.getName());
      log.info("파티 댓글 뷰단 유저번호 :{}",response.getId());
      log.info("파티 댓글 뷰단 party번호:{}",response.getPartyId());
      log.info("파티 댓글 뷰단 party번호:{}",response.getBody());

      PartyCommentResponse comment = partyCommentService.addPartyComment(response.getPartyId(), String.valueOf(response.getId()), response.getBody());

        return "user/login";
    }
}
