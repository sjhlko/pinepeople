package com.lion.pinepeople.mvc;

import com.lion.pinepeople.domain.dto.party.PartyInfoResponse;
import com.lion.pinepeople.domain.dto.partyComment.PartyCommentDeleteResponse;
import com.lion.pinepeople.domain.dto.partyComment.PartyCommentListResponse;
import com.lion.pinepeople.domain.dto.partyComment.PartyCommentResponse;
import com.lion.pinepeople.domain.dto.partyComment.PartyMvcCommentResponse;
import com.lion.pinepeople.domain.entity.PartyComment;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.service.PartyCommentService;
import com.lion.pinepeople.service.PartyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("pinepeople/party")
public class PartyCommentMvcController {

    private final PartyCommentService partyCommentService;
    private final PartyService partyService;

    /**댓글 작성**/
    @PostMapping("/write-comment")
    public String doPartyComment(Authentication authentication, PartyMvcCommentResponse response, Model model) {
      log.info("파티 댓글 뷰단 :{}",authentication.getName());
      log.info("파티 댓글 뷰단 유저번호 :{}",response.getId());
      log.info("파티 댓글 뷰단 party번호:{}",response.getPartyId());
      log.info("파티 댓글 뷰단 party번호:{}",response.getBody());

      PartyCommentResponse comment = partyCommentService.addPartyComment(response.getPartyId(), String.valueOf(response.getId()), response.getBody());

      return "redirect:/users/party/show-comment/"+response.getPartyId();
    }

    /**party 댓글 작성 후 보여주기**/
    @GetMapping("/show-comment/{partyId}")
    public String showPartyComment(@PathVariable Long partyId,Model model) {
        List<PartyComment> comments = partyCommentService.getCommentList(partyId);
        PartyInfoResponse party = partyService.getParty(partyId);
        model.addAttribute("party", party);
        model.addAttribute(new PartyComment());
        model.addAttribute("comments", comments);
        return "party/partyDetail";
    }

    /**파티 comment 삭제**/
    @GetMapping("/delete/{partyId}/{commentId}/{userId}")
    public String showPartyComment(Authentication authentication,@PathVariable Long partyId, @PathVariable Long commentId,@PathVariable Long userId, Model model, HttpServletResponse response) throws Exception{
        checkDeleteUser(authentication, userId, response);
        partyCommentService.deleteComment(partyId, commentId, authentication.getName());
        List<PartyComment> comments = partyCommentService.getCommentList(partyId);
        PartyInfoResponse party = partyService.getParty(partyId);
        model.addAttribute("party", party);
        model.addAttribute(new PartyComment());
        model.addAttribute("comments", comments);
        return "redirect:/pinepeople/party/detail/"+partyId;
    }


    /**삭제 권한 검사 메서드*/
    private void checkDeleteUser(Authentication authentication, @PathVariable Long userId, HttpServletResponse response) throws IOException {
        if(Long.parseLong(authentication.getName())!=userId){
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('댓글 삭제 권한이 없습니다.'); history.go(-1);</script>");
            out.flush();
        }
    }
}

