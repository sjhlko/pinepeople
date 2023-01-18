package com.lion.pinepeople.config.security;

import com.lion.pinepeople.config.security.exception.AuthenticationManager;
import com.lion.pinepeople.config.security.exception.CustomAccessDeniedHandler;
import com.lion.pinepeople.config.security.filter.JwtFilter;
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

    private final String[] PERMMIT = {
            "/swagger-ui/**"
    };
    private final String[] POST_AUTHENTICATED = {
    };

    private final String[] PATCH_AUTHENTICATED = {
            "/api/users/*"
    };

    private final String[] DELETE_AUTHENTICATED = {
            "/api/users/*"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers(PERMMIT).permitAll()
                .antMatchers(HttpMethod.POST, POST_AUTHENTICATED).authenticated()
                .antMatchers(HttpMethod.PATCH, PATCH_AUTHENTICATED).authenticated()
                .antMatchers(HttpMethod.DELETE, DELETE_AUTHENTICATED).authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationManager)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
