package com.lion.pinepeople.config.security.OAuth;

import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.repository.UserRepository;
import com.lion.pinepeople.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final UserService userService;

    @Value("${jwt.token.secret}")
    private String key;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            response.sendRedirect("/pinepeople/join?email="+email);
        }else{
            userService.oAuthLogin(email,response);
            response.sendRedirect("/pinepeople");
        }
    }
}
