package com.perfumepictor.dev.controller;

import com.perfumepictor.dev.dto.GetUserIdResponseDTO;
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
    public BaseResponse<GetUserIdResponseDTO> getUser() {
        String userId = authService.getCurrentUserId();
        return BaseResponse.onSuccess(GetUserIdResponseDTO.from(userId));
    }

}
