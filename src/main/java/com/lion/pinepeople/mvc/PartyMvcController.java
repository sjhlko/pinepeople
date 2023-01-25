package com.lion.pinepeople.mvc;

import com.lion.pinepeople.domain.dto.party.PartyInfoResponse;
import com.lion.pinepeople.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PartyMvcController {

    private final PartyService partyService;

    @GetMapping("/partyList")
    public String getPartyList(@PageableDefault(page = 0 ,size = 10, sort ="createdAt",
            direction = Sort.Direction.DESC) Pageable pageable, Model model,String title) {
        Page<PartyInfoResponse> partys = partyService.getAllParty(pageable);
        model.addAttribute("partys", partys);
        //new PostSelectResponse();
        //페이지블럭 처리
        //1을 더해주는 이유는 pageable은 0부터라 1을 처리하려면 1을 더해서 시작해주어야 한다.
        int nowPage = partys.getPageable().getPageNumber() + 1;
        //-1값이 들어가는 것을 막기 위해서 max값으로 두 개의 값을 넣고 더 큰 값을 넣어주게 된다.
        int startPage =  Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage+9, partys.getTotalPages());

        /**페이지 최신순 플래그🔽**/
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "partyList";
    }
}
