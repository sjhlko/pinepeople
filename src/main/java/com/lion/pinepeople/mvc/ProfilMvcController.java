package com.lion.pinepeople.mvc;

import com.lion.pinepeople.domain.dto.party.PartyInfoResponse;
import com.lion.pinepeople.domain.dto.user.login.UserLoginRequest;
import com.lion.pinepeople.domain.dto.user.myInfo.MyInfoResponse;
import com.lion.pinepeople.domain.dto.user.userInfo.UserInfoResponse;
import com.lion.pinepeople.enums.UserRole;
import com.lion.pinepeople.service.BrixService;
import com.lion.pinepeople.service.PartyService;
import com.lion.pinepeople.service.ReportService;
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
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/pinepeople")
public class ProfilMvcController {
    private final BrixService brixService;
    private final ReportService reportService;
    private final UserService userService;
    private final PartyService partyService;

    @GetMapping("/profile/myPage")
    public String getmyPage(@PageableDefault(page = 0 ,size = 10, sort ="createdAt", direction = Sort.Direction.DESC) Pageable pageable, Model model, Authentication authentication){

        if(authentication == null){
            model.addAttribute("userLoginRequest", new UserLoginRequest());
            return "redirect:/pinepeople/login";
        }

        //마이프로필 조회
        MyInfoResponse myInfo = userService.getMyInfo(authentication.getName());
        model.addAttribute("myInfo", myInfo);

        //내 파티 조회
//        Page<PartyInfoResponse> myHostPartys = partyService.getMyParty(pageable, "HOST", authentication.getName());
//        model.addAttribute("myHostPartys", myHostPartys);

        Page<PartyInfoResponse> myGuestPartys = partyService.getMyParty(pageable, "GUEST", authentication.getName());
        model.addAttribute("myGuestPartys", myGuestPartys);

        /**페이징 처리**/
        int nowPage = myGuestPartys.getPageable().getPageNumber() + 1;
        int startPage =  Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage+9, myGuestPartys.getTotalPages());

        /**페이지 최신순 플래그🔽**/
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "profil/myPage";
    }



//    @GetMapping("/profile/profilePage/{userId}")
//    public String getProfil(@PathVariable Long userId, Authentication authentication){
//        Double brix = brixService.getBrix(authentication.getName(), userId);
//        return "profil/profilePage";
//    }

    @GetMapping("/profile/adminPage")
    public String getAdminPage(){
        return "profil/adminPage";
    }
}
