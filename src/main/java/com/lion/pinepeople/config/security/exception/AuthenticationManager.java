package com.lion.pinepeople.config.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.ErrorResponse;
import com.lion.pinepeople.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationManager implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserService userService;

    /**
     * 유효하지 않는 토큰 형식이나 토큰 만료시 예외를 발생시키는 메서드
     *
     * @param request       that resulted in an <code>AuthenticationException</code>
     * @param response      so that the user agent can begin authentication
     * @param authException that caused the invocation
     * @throws IOException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = String.valueOf(request.getAttribute("exception"));
        if (exception.equals(ErrorCode.EXPIRE_TOKEN.name())) {
            if (userService.isReissueable(request, response)) {
                response.sendRedirect(request.getRequestURL().toString());
            } else {
                if (request.getRequestURL().toString().contains("api")) {
                    log.error(ErrorCode.EXPIRE_TOKEN.getMessage());
                    setResponse(ErrorCode.EXPIRE_TOKEN, response);
                } else response.sendRedirect("/pinepeople/login");
            }
        } else {
            if (request.getRequestURL().toString().contains("api")) {
                log.error(ErrorCode.INVALID_TOKEN.getMessage());
                setResponse(ErrorCode.INVALID_TOKEN, response);
            } else response.sendRedirect("/pinepeople/login");
        }
    }

    private void setResponse(ErrorCode errorCode, HttpServletResponse response) throws IOException {
        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        Response resultResponse = Response.error(new ErrorResponse(errorCode, errorCode.getMessage()));
        response.getWriter().write(objectMapper.writeValueAsString(resultResponse));
    }
}

