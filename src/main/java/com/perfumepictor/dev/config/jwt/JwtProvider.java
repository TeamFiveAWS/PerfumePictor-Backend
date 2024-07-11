package com.perfumepictor.dev.config.jwt;

import com.perfumepictor.dev.payload.code.status.ErrorStatus;
import com.perfumepictor.dev.payload.exception.GeneralException;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    public static String JWT_SET_URI;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    public void setJwkSetUri(String jwkSetUri) {
        JwtProvider.JWT_SET_URI = jwkSetUri;
    }

    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(JWT_SET_URI).build();
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        parseClaims(token);
        return true;
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = new PrincipalDetails(getUserId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserId(String token) {
        return parseClaims(token).get("username").toString();
    }

    private Map<String, Object> parseClaims(String token) {
        try {
            return jwtDecoder().decode(token).getClaims();
        } catch (ExpiredJwtException e) {
            throw new GeneralException(ErrorStatus.EXPIRED_TOKEN);
        } catch (Exception e) {
            log.error("JwtProvider::parseClaims::Invalid JWT token : ", e);
            throw new GeneralException(ErrorStatus.INVALID_JWT_TOKEN);
        }
    }
}
