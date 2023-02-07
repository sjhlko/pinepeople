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

    private final String[] SWAGGER = {
            "/swagger-ui/**"
    };
    private final String[] POST_PERMIT = {
            "/pinepeople/api/join",
            "/pinepeople/api/login",
            "/pinepeople/api/logout",
            "/pinepeople/login",
            "/pinepeople/logout",
            "/pinepeople/join",
            "/pinepeople/change-password"
    };

    private final String[] GET_AUTHENTICATED = {
            /* AdminController */
            "/pinepeople/api/admin/black-lists/*",
            "/pinepeople/api/admin/black-lists",
            /* AlarmController */
            "/pinepeople/api/alarms/my",
            /*BrixContoroler*/
            "/pinepeople/api/users/*/brix",
            /* CommentController */
            "/pinepeople/api/posts/*/comments/my",
            /* OrderController */
            "/pinepeople/api/users/order-lists/*",
            "/pinepeople/api/users/order-lists/my",
            /* ParticipantController */
            "/pinepeople/api/partys/*/participants/waits",
            /* PartyController */
            "/pinepeople/api/partys/my",
            "/pinepeople/api/partys/my-waitings",
            /* PostController */
            "/pinepeople/api/posts/my",
            /* UserController */
            "/pinepeople/api/my",
            /* OrderMvcController */
            "/pinepeople/party/*/order",
            "/pinepeople/party/order-detail/*",
            "/pinepeople/party/order-list",
            /* PartyMvcController */
            "/pinepeople/party/join/*",
            "/pinepeople/party/create-new",
            /* ProfileMvcContorller */
            "/pinepeople/profile/myPage",
            "/pinepeople/profile/profilePage/*",
            /* UserMvcContorller */
            "/pinepeople/profile/myPage/update",
            "/pinepeople/myInfo"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic().disable()
                .csrf().disable()
                .cors().and();

        httpSecurity.authorizeRequests()
                .antMatchers(SWAGGER).permitAll()
                .antMatchers(POST_PERMIT).permitAll()
                .antMatchers(HttpMethod.GET, GET_AUTHENTICATED).authenticated()
                .antMatchers(HttpMethod.POST).authenticated()
                .antMatchers(HttpMethod.PUT).authenticated()
                .antMatchers(HttpMethod.PATCH).authenticated()
                .antMatchers(HttpMethod.DELETE).authenticated()
                .antMatchers("/pinepeople/profile/adminPage").access("hasRole('ADMIN')");

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
