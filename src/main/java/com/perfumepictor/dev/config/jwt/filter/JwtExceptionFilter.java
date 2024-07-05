package com.perfumepictor.dev.config.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfumepictor.dev.payload.BaseResponse;
import com.perfumepictor.dev.payload.code.status.ErrorStatus;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            log.error("JwtExceptionFilter::doFilterInternal::JwtException : {}", e);
            setResponse(response, ErrorStatus.INVALID_JWT_TOKEN, HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            log.error("JwtExceptionFilter::doFilterInternal::Exception : {}", e);
            setResponse(response, ErrorStatus._INTERNAL_SERVER_ERROR, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void setResponse(HttpServletResponse response, ErrorStatus errorStatus, int status) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        BaseResponse<ErrorStatus> apiResponse = BaseResponse.onFailure(errorStatus.getCode(), errorStatus.getMessage(), null);
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        response.getWriter().write(jsonResponse);
    }
}
