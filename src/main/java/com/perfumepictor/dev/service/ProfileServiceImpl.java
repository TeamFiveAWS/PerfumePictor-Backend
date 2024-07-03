package com.perfumepictor.dev.service;

import com.perfumepictor.dev.entity.Profiles;
import com.perfumepictor.dev.payload.code.status.ErrorStatus;
import com.perfumepictor.dev.payload.exception.GeneralException;
import com.perfumepictor.dev.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final AuthService authService;
    private final ProfileRepository profileRepository;

    public void createProfile() {
         profileRepository.insertProfile(Profiles.builder()
                .email(authService.getCurrentUserEmail())
                .build());
    }

    public Profiles getProfile() {

        String email = authService.getCurrentUserEmail();
        System.out.println("email = " + email);

        return profileRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PROFILE_NOT_FOUND));
    }
}
