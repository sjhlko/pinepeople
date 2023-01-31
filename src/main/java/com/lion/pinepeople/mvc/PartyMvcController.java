package com.lion.pinepeople.mvc;

import com.lion.pinepeople.domain.dto.party.PartyInfoResponse;
import com.lion.pinepeople.domain.entity.Category;
import com.lion.pinepeople.domain.entity.Party;
import com.lion.pinepeople.domain.entity.PartyComment;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.PartyRepository;
import com.lion.pinepeople.repository.UserRepository;
import com.lion.pinepeople.service.CategoryService;
import com.lion.pinepeople.service.PartyService;
import com.lion.pinepeople.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("users/party")
public class PartyMvcController {

    private final CategoryService categoryService;
    private final PartyService partyService;
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
        categoryService.doCategory(model);
        doPage(model, partys);
        return "party/partyList";
    }


    /**파티 상세보기**/
    @GetMapping("/detail/{id}")
    public String getPartyDetail(@PathVariable Long id, Model model, Authentication authentication) {
        log.info("로그인 파트-----------------------");
        log.info("id:{}", id);
        PartyInfoResponse party = partyService.getParty(id);
        log.info("로그인한 회원의 id:{}",  authentication.getName());
        User user = getUser(authentication);
        model.addAttribute("party", party);
        model.addAttribute("user", user);
        model.addAttribute(new PartyComment());
        return "party/partyDetail";
    }




    @GetMapping("/category/{name}")
    public String getCategoryParties(@PathVariable String name,Model model,@PageableDefault(page = 0, size = 5, sort = "createdAt",
            direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Party> parties = partyRepository.findByCategory_Name(pageable, name);
        Page<PartyInfoResponse> partys = PartyInfoResponse.toPage(parties);
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

    private User getUser(Authentication authentication) {
        long userId = Long.parseLong(authentication.getName());
        return userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }


}
