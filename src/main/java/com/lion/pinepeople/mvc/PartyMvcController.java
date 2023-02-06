package com.lion.pinepeople.mvc;

import com.lion.pinepeople.domain.dto.participant.ParticipantCreateResponse;
import com.lion.pinepeople.domain.dto.party.PartyCategoryRequest;
import com.lion.pinepeople.domain.dto.party.PartyCreateRequest;
import com.lion.pinepeople.domain.dto.party.PartyInfoResponse;
import com.lion.pinepeople.domain.dto.party.PartyUpdateRequest;
import com.lion.pinepeople.domain.dto.partyComment.PartyCommentResponse;
import com.lion.pinepeople.domain.dto.partyComment.PartyMvcCommentResponse;
import com.lion.pinepeople.domain.entity.Category;
import com.lion.pinepeople.domain.entity.Party;
import com.lion.pinepeople.domain.entity.PartyComment;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.PartyRepository;
import com.lion.pinepeople.repository.UserRepository;
import com.lion.pinepeople.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("pinepeople/party")
public class PartyMvcController {

    private final CategoryService categoryService;
    private final PartyService partyService;
    private final ParticipantService participantService;
    private final PartyCommentService partyCommentService;
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;

    /**파티 리스트 페이지**/
    @GetMapping("/list")
    public String getPartyList(@PageableDefault(page = 0, size = 5, sort = "createdAt",
            direction = Sort.Direction.DESC) Pageable pageable, Model model, String address, String partyContent, String partyTitle) {
        Page<PartyInfoResponse> partys = null;
        if (address == null || partyTitle == null) {
           partys = partyService.getAllParty(pageable);
            model.addAttribute("partys", partys);
        }
        if (address!=null || partyTitle!=null) {
            partys = partyService.searchParty(pageable, address, partyContent, partyTitle);
            model.addAttribute("partys", partys);
        }
        //카테고리 검색🔽
        //categoryService.doCategory(model);
        model.addAttribute("rightNows", categoryService.getCategorySteadily("Right now!",1));
        model.addAttribute("steadilys", categoryService.getCategorySteadily("Steadily!",1));
        doPage(model, partys);
        return "party/partyList";
    }


    /**파티 상세보기**/
    @GetMapping("/detail/{id}")
    public String getPartyDetail(@PathVariable Long id, Model model, Authentication authentication) {
        log.info("로그인 파트-----------------------");
        log.info("id:{}", id);
        List<PartyComment> comments = partyCommentService.getCommentList(id);
        if (authentication == null) {
            PartyInfoResponse party = partyService.getParty(id);
            model.addAttribute("party", party);
            model.addAttribute("comments", comments);
            model.addAttribute(new PartyComment());
            return "party/partyDetailNonLogin";
        }
        PartyInfoResponse party = partyService.getParty(id);
        User user = getUser(authentication);
        model.addAttribute("party", party);
        model.addAttribute("user", user);
        model.addAttribute("comments", comments);
        model.addAttribute(new PartyComment());
        return "party/partyDetail";
    }

    /**파티 참가**/
    @GetMapping("/join/{id}")
    public String doPartyComment(Authentication authentication, @PathVariable Long id, HttpServletResponse response) throws IOException {
        try {
            participantService.createGuestParticipant(id,authentication.getName());
            printMessage("가입 신청 되었습니다.",response);
        } catch (AppException e){
            printMessage(e.getMessage(),response);
        }
        return "redirect:/pinepeople/party/detail/"+id;
    }


    /**상세 카테고리별 파티 조회**/
    @GetMapping("/category/{categoryName}")
    public String getCategoryParties(@PathVariable String categoryName,Model model,@PageableDefault(page = 0, size = 5, sort = "createdAt",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PartyInfoResponse> partys = partyService.getPartyByCategory(pageable,categoryName);
        model.addAttribute("partys", partys);
        categoryService.doCategory(model);
        doPage(model, partys);
        return "party/partyList";

    }

    @GetMapping("/category")
    public String getBranchParties(@RequestParam String branch,Model model,@PageableDefault(page = 0, size = 5, sort = "createdAt",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PartyInfoResponse> partys = partyService.getPartyByCategoryBranch(pageable,branch);
        model.addAttribute("partys", partys);
        categoryService.doCategory(model);
        doPage(model, partys);
        return "party/partyList";

    }


    /**페이징 정보 model로 넘기는 메서드**/
    private void doPage(Model model, Page<PartyInfoResponse> partys) {
        /**페이징 처리**/
        int nowPage = partys.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 9, partys.getTotalPages());
        /**페이지 **/
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
    }

    /**
     * 글 작성 페이지 접근 메소드
     * */
    @GetMapping("/create-new")
    public String getCreateParty(Model model, Authentication authentication) {
        System.out.println(authentication.getName());
        model.addAttribute("partyCreateRequest", new PartyCategoryRequest());
        model.addAttribute("rightNowCategory", categoryService.getCategorySteadily("Right now!",1));
        model.addAttribute("steadilyCategory", categoryService.getCategorySteadily("Steadily!",1));
        return "party/partyCreate";
    }

    /**
     * 글 작성 메소드
     * */
    @PostMapping("/create-new")
    public String createParty(Authentication authentication, @Validated @ModelAttribute PartyCategoryRequest partyCategoryRequest,
                              @RequestParam String branch, @RequestParam String code) {
        try {
            PartyCategoryRequest request = PartyCategoryRequest.of(partyCategoryRequest,branch,code.split(",")[0]);
            if(branch.equals("Steadily!")){
                request = PartyCategoryRequest.of(partyCategoryRequest,branch,code.split(",")[1]);
            }
            partyService.createPartyWithCategory(request,authentication.getName());
        } catch (AppException e) {
            log.info("파티 생성 실패 : {}", e.getErrorCode());
        }
        return "redirect:/pinepeople/party/list";
    }

    /**
     * 글 수정 메소드
     * */
//    @PatchMapping("/update/{id}")
//    public String updateParty(Authentication authentication, @Validated @ModelAttribute PartyUpdateRequest partyUpdateRequest,
//                              @PathVariable Long id, Model model){
//        try {
//            partyService.createPartyWithCategory(request,authentication.getName());
//        } catch (AppException e) {
//            log.info("파티 생성 실패 : {}", e.getErrorCode());
//        }
//        return "redirect:/pinepeople/party/list";
//
//    }

    private User getUser(Authentication authentication) {
        long userId = Long.parseLong(authentication.getName());
        return userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private void printMessage(String message, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>alert('"+message+"');history.go(-1);</script>");
        out.flush();
    }


}
