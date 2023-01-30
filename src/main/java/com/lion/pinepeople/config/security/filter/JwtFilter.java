package com.lion.pinepeople.config.security.filter;

import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.repository.UserRepository;
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
import javax.servlet.http.Cookie;
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

//        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
//        log.info("authorization : {}", authorization);
//
//        if (authorization == null || !authorization.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = authorization.split(" ")[1];
//        log.info("token : {}", token);

        String token = "token";

        Cookie[] list = request.getCookies();

        if(list==null){
            log.info("쿠키가 존재하지 않습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        for (Cookie cookie:list) {
            if(cookie.getName().equals("token")){
                token = cookie.getValue();
            }
        }

        log.info("token : {}",token);
        if (!JwtTokenUtil.isValidToken(request, token, key)) {
            filterChain.doFilter(request, response);
            return;
        }

        User findUser = userRepository.findById(JwtTokenUtil.getUserId(token, key)).get();
        log.info("userRole : {}", findUser.getRole().name());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(JwtTokenUtil.getUserId(token, key), null, List.of(new SimpleGrantedAuthority("ROLE_" + findUser.getRole().name())));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);

    }
}
