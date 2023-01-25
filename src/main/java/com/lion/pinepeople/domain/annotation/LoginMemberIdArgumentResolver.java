package com.lion.pinepeople.domain.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lion.pinepeople.config.security.filter.JwtHelper;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Base64;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginMemberIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMemberId.class)
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
       /* String accessToken = webRequest.getHeader("Authorization").split("Bearer ")[1];
        return tokenService.(accessToken);*/

        String authorizationHeader = webRequest.getHeader("Authorization");
        log.info("Authorization Header ::: " + authorizationHeader);

        if (authorizationHeader == null) {
            throw new AppException(ErrorCode.EXPIRE_TOKEN);
        }

        String jwtToken = authorizationHeader.substring(7);
        Base64.Decoder decoder = Base64.getUrlDecoder();
        byte[] decodedToken = decoder.decode(jwtToken);
        Map<String, String> claims = objectMapper.readValue(decodedToken, Map.class);
        String userId = claims.get("userId");
        log.info("Decoded email is ::: " + userId);
        return userId;
    }
}
