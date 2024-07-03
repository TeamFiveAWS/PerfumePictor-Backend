package com.perfumepictor.dev.controller;

import com.perfumepictor.dev.entity.Profiles;
import com.perfumepictor.dev.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("api/v1/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping
    public void createProfile() {
        profileService.createProfile();
    }

    @GetMapping
    public Profiles getProfiles() {
        return profileService.getProfile();
    }
}
