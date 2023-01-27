package com.lion.pinepeople.mvc;

import com.lion.pinepeople.domain.dto.party.PartyInfoResponse;
import com.lion.pinepeople.domain.entity.Category;
import com.lion.pinepeople.service.CategoryService;
import com.lion.pinepeople.service.PartyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PartyMvcController {

    private final PartyService partyService;
    private final CategoryService categoryService;

    @GetMapping("/party/list")
    public String getPartyList(@PageableDefault(page = 0 ,size = 10, sort ="createdAt",
            direction = Sort.Direction.DESC) Pageable pageable, Model model,String title,String branch) {
        Page<PartyInfoResponse> partys = partyService.getAllParty(pageable);
        model.addAttribute("partys", partys);


        List<Category> rightNows = categoryService.getCategorySteadily("RightNow!",1);
        List<Category> steadilys = categoryService.getCategorySteadily("Steadily!",1);
        model.addAttribute("rightNows", rightNows);
        model.addAttribute("steadilys", steadilys);

        /**페이징 처리**/
        int nowPage = partys.getPageable().getPageNumber() + 1;
        int startPage =  Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage+9, partys.getTotalPages());
        /**페이지 최신순 플래그🔽**/
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "party/partyList";
    }

    @GetMapping("/party/detail/{id}")
    public String getPartyList(@PathVariable Long id, Model model, String title) {
        log.info("id:{}",id);
        PartyInfoResponse party = partyService.getParty(id);
        model.addAttribute("party", party);
        return "party/partyDetail";
    }


}
