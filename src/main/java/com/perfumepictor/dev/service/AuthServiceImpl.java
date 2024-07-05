package com.perfumepictor.dev.service;

import com.perfumepictor.dev.payload.code.status.ErrorStatus;
import com.perfumepictor.dev.payload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    public String getCurrentUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new GeneralException(ErrorStatus.INVALID_JWT_TOKEN);
        }

        return authentication.getName();
    }

}
