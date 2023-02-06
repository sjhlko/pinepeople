package com.lion.pinepeople.mvc;

import com.lion.pinepeople.domain.dto.user.ChangePasswordRequest;
import com.lion.pinepeople.domain.dto.user.join.UserJoinRequest;
import com.lion.pinepeople.domain.dto.user.login.UserLoginRequest;
import com.lion.pinepeople.domain.dto.user.myInfo.MyInfoResponse;
import com.lion.pinepeople.domain.dto.user.update.UserUpdateRequest;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.service.SmsService;
import com.lion.pinepeople.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Controller
@Slf4j
@RequestMapping("/pinepeople")
@RequiredArgsConstructor
public class UserMvcController {

    private final UserService userService;
    private final SmsService smsService;

//    /**
//     * 쿠키가 잘 들어왔는지 확인하기 위한 메서드
//     *
//     * @param model          userservice의 getMyInfo메서드에서 반환받은 response를 login-test페이지로 넘긴다.
//     * @param authentication 로그인 성공시 사용자 정보가 담겨있다.
//     * @return authentication이 null이면 test페이지 반환 아니면 login-test 페이지를 반환
//     */
//    @GetMapping
//    public String test(Model model, Authentication authentication) {
//        if (!Objects.isNull(authentication)) {
//            MyInfoResponse myInfoResponse = userService.getMyInfo(authentication.getName());
//            model.addAttribute("myInfo", myInfoResponse);
//            return "user/login-test";
//        }
//        return "user/test";
//    }
//    @GetMapping
//    public String mainPage(Model model, Authentication authentication){
//        if(!Objects.isNull(authentication)){
//            MyInfoResponse myInfoResponse = userService.getMyInfo(authentication.getName());
//            model.addAttribute("myInfo", myInfoResponse);
//        }else{
//            log.info("non login");
//            model.addAttribute("myInfo", null);
//        }
//        return "main/mainPage";
//    }

    @GetMapping
    public String mainPage(){
        return "main/mainPage";
    }

    /**
     * 로그인 페이지 불러오는 메서드
     *
     * @param model UserLoginRequest 객체를 타임리프로 넘긴다.
     * @return login 페이지를 반환한다.
     */
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userLoginRequest", new UserLoginRequest());
        return "user/login";
    }

    /**
     * 로그인 페이지에서 입력한 정보를 가지고 로그인을 하는 메서드
     *
     * @param userLoginRequest 로그인 요청 dto
     * @param response         쿠키를 설정하기 위해 response를 서비스로 넘긴다.
     * @return 로그인 성공하면 login-test 페이지로 리다이렉트한다.
     */
    @PostMapping("/login")
    public String doLogin(@Validated @ModelAttribute UserLoginRequest userLoginRequest, BindingResult bindingResult, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            return "user/login";
        }

        try {
            userService.login(userLoginRequest, response);
        } catch (AppException e) {
            log.info("로그인 실패 {}", e.getMessage());
            bindingResult.reject("loginFail", e.getMessage());
        }

        if (bindingResult.hasErrors()) {
            return "user/login";
        }

        return "redirect:/pinepeople";
    }

    /**
     * 로그아웃을 하는 메서드
     *
     * @param response 쿠키를 설정하기 위해 response를 서비스로 넘긴다.
     * @return 로그아웃 이후 test 페이지로 리다이렉트한다.
     */
    @PostMapping("/logout")
    public String doLogout(HttpServletRequest request, HttpServletResponse response) {
        userService.logout(request, response);
        return "redirect:/pinepeople";
    }

    /**
     * 회원 가입 폼 호출 메서드
     *
     * @param model userJoinRequest를 넘겨준다.
     * @return join 페이지를 반환한다.
     */
    @GetMapping("/join")
    public String join(@RequestParam(required = false) String email, Model model) {
        UserJoinRequest userJoinRequest = new UserJoinRequest();
        if (email != null) {
            userJoinRequest.setEmail(email);
        }
        model.addAttribute("userJoinRequest", userJoinRequest);
        return "user/join";
    }

    /**
     * 회원가입을 진행하는 메서드
     *
     * @param userJoinRequest 회원가입을 위한 사용자 정보를 담는 dto
     * @param bindingResult   사용자 정보 유효성 체크
     * @return 회원가입에 성공하면 로그인 페이지로 리다이렉트한다.
     */
    @PostMapping("/join")
    public String doJoin(@Validated @ModelAttribute UserJoinRequest userJoinRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "user/join";
        }

        try {
            userService.join(userJoinRequest);
        } catch (AppException e) {
            log.info("회원가입 실패 {}", e.getMessage());
            bindingResult.reject("joinFail", e.getMessage());
        }

        if (bindingResult.hasErrors()) {
            return "user/join";
        }

        return "redirect:/pinepeople/login";
    }

    @GetMapping("/find-email-form")
    public String FindEmail() {
        return "user/findEmail";
    }

    @GetMapping("/change-password-form")
    public String ChangePassword(Model model) {
        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());
        return "user/changePassword";
    }

    @GetMapping("/sendSMS")
    public @ResponseBody String sendSMS(@RequestParam(value = "to") String to) {
        String random = smsService.randNum();
        SingleMessageSentResponse singleMessageSentResponse = smsService.sendOne(to, random);
        return random;
    }

    @GetMapping(value = "/find-email", produces = "application/text; charset=utf8")
    public @ResponseBody String doFindEmail(@RequestParam(value = "phone") String phone, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        return userService.findEmail(phone);
    }

    @PostMapping("/change-password")
    public String changePassword(@Validated @ModelAttribute ChangePasswordRequest changePasswordRequest, BindingResult bindingResult, Model model) {

        log.info("change-password api");
        if (bindingResult.hasErrors()) {
            return "user/changePassword";
        }

        try {
            model.addAttribute("changePasswordSuccess", userService.changePassword(changePasswordRequest));
            model.addAttribute("userLoginRequest", new UserLoginRequest());
        } catch (AppException e) {
            bindingResult.reject("changePasswordFail", e.getMessage());
        }

        if (bindingResult.hasErrors()) {
            return "user/changePassword";
        }

        return "user/login";
    }

    @GetMapping("/profile/myPage/update")
    public String updateMyPageForm(Authentication authentication, Model model) {
        model.addAttribute("userUpdateRequest", new UserUpdateRequest());
        return "profile/updateMyPage";
    }

    @GetMapping("/myInfo")
    public @ResponseBody MyInfoResponse getMyInfo(Authentication authentication) {
        MyInfoResponse myInfoResponse = userService.getMyInfo(authentication.getName());
        return myInfoResponse;
    }

    @PostMapping("/profile/myPage/update")
    public String updateMyInfo(@Validated @ModelAttribute UserUpdateRequest userUpdateRequest, BindingResult bindingResult, Authentication authentication) {

        if (bindingResult.hasErrors()) {
            return "profile/updateMyPage";
        }

        try {
            userService.modify(authentication.getName(),userUpdateRequest);
        } catch (AppException e) {
            bindingResult.reject("userUpdateFail", e.getMessage());
        }

        if (bindingResult.hasErrors()) {
            return "profile/updateMyPage";
        }

        return "redirect:/pinepeople/profile/myPage";
    }

}
