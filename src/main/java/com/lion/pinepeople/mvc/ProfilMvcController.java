package com.lion.pinepeople.mvc;

import com.lion.pinepeople.domain.dto.party.PartyInfoResponse;
import com.lion.pinepeople.domain.dto.user.login.UserLoginRequest;
import com.lion.pinepeople.domain.dto.user.myInfo.MyInfoResponse;
import com.lion.pinepeople.domain.dto.user.update.UserUpdateRequest;
import com.lion.pinepeople.domain.dto.user.userInfo.UserInfoResponse;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/pinepeople")
public class ProfilMvcController {

    private final UserService userService;
    private final AdminService adminService;

    @GetMapping("/profile/myPage")
    public String getmyPage(@PageableDefault(page = 0 ,size = 10, sort ="createdAt", direction = Sort.Direction.DESC) Pageable pageable, Model model, Authentication authentication){

        if(authentication == null){
            model.addAttribute("userLoginRequest", new UserLoginRequest());
            return "redirect:/pinepeople/login";
        }

        //마이프로필 조회
        MyInfoResponse myInfo = userService.getMyInfo(authentication.getName());
        model.addAttribute("myInfo", myInfo);

        return "profile/myPage";
    }



    @GetMapping("/profile/profilePage/{userId}")
    public String getProfil(@PathVariable Long userId, Model model, Authentication authentication){
        UserInfoResponse userInfo = userService.getUserInfo(userId);
        log.info(userInfo.getProfileImg());
        model.addAttribute("userInfo", userInfo);
        return "profile/profilePage";
    }

    @GetMapping("/profile/adminPage")
    public String getAdminPage(Authentication authentication){
        adminService.isAdmin(authentication.getName());
        return "profile/adminPage";
    }

    @GetMapping("/profile/myPage/update")
    public String updateMyPageForm(Authentication authentication, Model model) {
        model.addAttribute("userUpdateRequest", new UserUpdateRequest());
        return "profile/updateMyPage";
    }

    @PostMapping("/profile/myPage/update")
    public String updateMyInfo(@Validated @ModelAttribute UserUpdateRequest userUpdateRequest, @RequestPart(value = "file") MultipartFile file,  BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "profile/updateMyPage";
        }

        try {
            userService.modify(authentication.getName(),userUpdateRequest, file);
        } catch (AppException e) {
            bindingResult.reject("userUpdateFail", e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (bindingResult.hasErrors()) {
            return "profile/updateMyPage";
        }

        return "redirect:/pinepeople/profile/myPage";
    }
}
