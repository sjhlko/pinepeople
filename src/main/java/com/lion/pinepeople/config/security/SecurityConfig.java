package com.lion.pinepeople.config.security;

import com.lion.pinepeople.config.security.OAuth.OAuth2SuccessHandler;
import com.lion.pinepeople.config.security.exception.AuthenticationManager;
import com.lion.pinepeople.config.security.exception.CustomAccessDeniedHandler;
import com.lion.pinepeople.config.security.filter.JwtFilter;
import com.lion.pinepeople.service.OAuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationManager authenticationManager;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final JwtFilter jwtFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    private final String[] PERMMIT = {
            "/swagger-ui/**",
            "/api/join",
            "/api/login",
            "/api/logout",
            "/pinepeople/login",
            "/pinepeople/logout",
            "/pinepeople/join",
            "/pinepeople/change-password"
    };

    private final String[] GET_AUTHENTICATED = {
            "/api/my",
            "/pinepeople",
            "/pinepeople/myPage/update",
            "/pinepeople/myInfo"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic().disable()
                .csrf().disable()
                .cors().and();

        httpSecurity.authorizeRequests()
                .antMatchers(PERMMIT).permitAll()
                .antMatchers(HttpMethod.GET, GET_AUTHENTICATED).authenticated()
                .antMatchers(HttpMethod.POST).authenticated()
                .antMatchers(HttpMethod.PUT).authenticated()
                .antMatchers(HttpMethod.PATCH).authenticated()
                .antMatchers(HttpMethod.DELETE).authenticated();

        httpSecurity.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.exceptionHandling()
                .authenticationEntryPoint(authenticationManager)
                .accessDeniedHandler(accessDeniedHandler);

        httpSecurity.oauth2Login().userInfoEndpoint().userService(new OAuthUserService());

        httpSecurity.oauth2Login().successHandler(oAuth2SuccessHandler);

        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}
