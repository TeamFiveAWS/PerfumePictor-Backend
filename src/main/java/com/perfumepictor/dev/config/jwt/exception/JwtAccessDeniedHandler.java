package com.perfumepictor.dev.config.jwt.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfumepictor.dev.payload.BaseResponse;
import com.perfumepictor.dev.payload.code.status.ErrorStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        if (request.getRequestURI().startsWith("/api/v1/auth/")) {
            log.info("[접근 권한 X] URL : [{}]", request.getRequestURL());

            ObjectMapper objectMapper = new ObjectMapper();
            String code = ErrorStatus.ACCESS_DENIED.getCode();
            String message = ErrorStatus.ACCESS_DENIED.getMessage();

            BaseResponse<ErrorStatus> apiResponse = BaseResponse.onFailure(code, message, null);
            String jsonResponse = objectMapper.writeValueAsString(apiResponse);

            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(jsonResponse);
        }
    }
}