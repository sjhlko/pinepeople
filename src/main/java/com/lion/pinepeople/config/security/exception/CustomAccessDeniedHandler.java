package com.lion.pinepeople.config.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 접근 권한이 없는 api 접근시 예외 발생 메서드
     *
     * @param request               that resulted in an <code>AccessDeniedException</code>
     * @param response              so that the user agent can be advised of the failure
     * @param accessDeniedException that caused the invocation
     * @throws IOException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.error("ADMIN 게정이 아닙니다.");
        if(request.getRequestURL().toString().contains("api")) {
            setResponse(ErrorCode.INVALID_PERMISSION, response);
        }else {
            //로그인 이후 관리자 페이지로 리다이렉트 할 수 있는 방법을 알아볼 예정
            response.sendRedirect("/pinepeople");
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
