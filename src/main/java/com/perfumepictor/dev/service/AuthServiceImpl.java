package com.perfumepictor.dev.service;

import com.perfumepictor.dev.config.jwt.PrincipalDetails;
import com.perfumepictor.dev.entity.Profiles;
import com.perfumepictor.dev.payload.code.status.ErrorStatus;
import com.perfumepictor.dev.payload.exception.GeneralException;
import com.perfumepictor.dev.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService, UserDetailsService {

    private final ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Profiles profile = profileRepository.findByEmail(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PROFILE_NOT_FOUND));

        log.info("user Email [ID : {}]", profile.getEmail());
        return new PrincipalDetails(profile);
    }

    public String getCurrentUserEmail() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new GeneralException(ErrorStatus.INVALID_JWT_TOKEN);
        }

        return authentication.getName();
    }
}
