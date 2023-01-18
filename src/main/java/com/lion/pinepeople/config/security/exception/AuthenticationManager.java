package com.lion.pinepeople.config.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class AuthenticationManager implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

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
        if (exception != null) {
            log.error(ErrorCode.EXPIRE_TOKEN.getMessage());
            setResponse(ErrorCode.EXPIRE_TOKEN, response);
        } else {
            log.error(ErrorCode.INVALID_TOKEN.getMessage());
            setResponse(ErrorCode.INVALID_TOKEN, response);
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

