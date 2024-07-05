package com.perfumepictor.dev.controller;

import com.perfumepictor.dev.converter.AuthConverter;
import com.perfumepictor.dev.dto.AuthResponseDTO;
import com.perfumepictor.dev.payload.BaseResponse;
import com.perfumepictor.dev.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/users")
    public BaseResponse<AuthResponseDTO.GetUserIdDTO> getUser() {
        String userId = authService.getCurrentUserId();
        return BaseResponse.onSuccess(AuthConverter.toGetUserIdDTO(userId));
    }

}
