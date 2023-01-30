package com.lion.pinepeople.mvc;

import com.lion.pinepeople.domain.dto.user.login.UserLoginRequest;
import com.lion.pinepeople.domain.dto.user.myInfo.MyInfoResponse;
import com.lion.pinepeople.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserMvcController {

    private final UserService userService;

    /**
     * 쿠키가 잘 들어왔는지 확인하기 위한 메서드
     * @param model userservice의 getMyInfo메서드에서 반환받은 response를 login-test페이지로 넘긴다.
     * @param authentication 로그인 성공시 사용자 정보가 담겨있다.
     * @return authentication이 null이면 test페이지 반환 아니면 login-test 페이지를 반환
     */
    @GetMapping
    public String test(Model model, Authentication authentication) {
        if (!Objects.isNull(authentication)) {
            MyInfoResponse myInfoResponse = userService.getMyInfo(authentication.getName());
            model.addAttribute("myInfo", myInfoResponse);
            return "user/login-test";
        }
        return "user/test";
    }

    /**
     * 로그인 페이지 불러오는 메서드
     * @param model UserLoginRequest 객체를 타임리프로 넘긴다.
     * @return login 페이지를 반환한다.
     */
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("useLoginRequest", new UserLoginRequest());
        return "user/login";
    }

    /**
     * 로그인 페이지에서 입력한 정보를 가지고 로그인을 하는 메서드
     * @param userLoginRequest 로그인 요청 dto
     * @param response 쿠키를 설정하기 위해 response를 서비스로 넘긴다.
     * @return 로그인 성공하면 login-test 페이지로 리다이렉트한다.
     */
    @PostMapping("/login")
    public String doLogin(UserLoginRequest userLoginRequest, HttpServletResponse response) {
        userService.login(userLoginRequest, response);
        return "redirect:/users";
    }

    /**
     * 로그아웃을 하는 메서드
     * @param response 쿠키를 설정하기 위해 response를 서비스로 넘긴다.
     * @return 로그아웃 이후 test 페이지로 리다이렉트한다.
     */
    @PostMapping("/logout")
    public String doLogout(HttpServletResponse response){
        userService.logout(response);
        return "redirect:/users";
    }
}
