package com.lion.pinepeople.config.security.filter;

import com.lion.pinepeople.config.security.userdetail.UserPrinclial;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.repository.UserRepository;
import com.lion.pinepeople.utils.CookieUtil;
import com.lion.pinepeople.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    @Value("${jwt.token.secret}")
    private String key;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = CookieUtil.getCookieValue(request, "token");

        if (token == null) {
            //log.info("쿠키가 존재하지 않습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("token : {}", token);

        String validTokenCheck = JwtTokenUtil.isValid(token, key);
        if (validTokenCheck.equals(ErrorCode.EXPIRE_TOKEN.name())) {
            request.setAttribute("exception", ErrorCode.EXPIRE_TOKEN);
            filterChain.doFilter(request, response);
            return;
        } else if (validTokenCheck.equals(ErrorCode.INVALID_TOKEN.name())) {
            request.setAttribute("exception", ErrorCode.INVALID_TOKEN);
            filterChain.doFilter(request, response);
            return;
        }

        User findUser = userRepository.findById(JwtTokenUtil.getUserId(token, key)).get();
        UserPrinclial userPrinclial = new UserPrinclial(findUser);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrinclial, null, List.of(new SimpleGrantedAuthority("ROLE_" + userPrinclial.getRole())));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
