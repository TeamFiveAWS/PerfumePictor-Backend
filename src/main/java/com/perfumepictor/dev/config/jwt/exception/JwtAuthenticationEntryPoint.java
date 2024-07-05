package com.perfumepictor.dev.config.jwt.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfumepictor.dev.payload.BaseResponse;
import com.perfumepictor.dev.payload.code.status.ErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        log.info("[인증 X] MyAuthenticationEntryPoint", authException.getMessage());

        ObjectMapper objectMapper = new ObjectMapper();
        String code = ErrorStatus.INVALID_JWT_TOKEN.getCode();
        String message = ErrorStatus.INVALID_JWT_TOKEN.getMessage();

        BaseResponse<ErrorStatus> apiResponse = BaseResponse.onFailure(code, message, null);
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonResponse);
    }
}