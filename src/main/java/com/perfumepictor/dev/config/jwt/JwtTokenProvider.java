package com.perfumepictor.dev.config.jwt;

import com.perfumepictor.dev.payload.code.status.ErrorStatus;
import com.perfumepictor.dev.payload.exception.GeneralException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    public static final String TOKEN_PREFIX = "Bearer ";

    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        Map<String, Object> claims = this.parseClaims(token);
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ssZ");
//        Date date;
//        try {
//            date = format.parse(claims.get("exp").toString());
//        } catch (ParseException e) {
//            throw new GeneralException(ErrorStatus.INVALID_JWT_TOKEN);
//        }
        Instant expiration = (Instant) claims.get("exp");
        return expiration.isAfter(Instant.now());
    }

    public Authentication getAuthentication(String token) {
        String username = this.getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveTokenFromRequest(String token) {
        if (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    private String getUsername(String token) {
        return this.parseClaims(token).get("sub").toString();
    }

    private Map<String, Object> parseClaims(String token) {
        try {
            Map<String, Object> map = jwtDecoder().decode(token).getClaims();
            return jwtDecoder().decode(token).getClaims();
        } catch (ExpiredJwtException e) {
            throw new GeneralException(ErrorStatus.TOKEN_TIME_OUT);
        }
//        } catch (Exception e) {
//            System.out.println("JwtTokenProvider::parseClaims: " + e);
//            throw new GeneralException(ErrorStatus.INVALID_JWT_TOKEN);
//        }
    }
}
