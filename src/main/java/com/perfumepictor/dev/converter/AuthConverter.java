package com.perfumepictor.dev.converter;

import com.perfumepictor.dev.dto.AuthResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthConverter {

    public static AuthResponseDTO.GetUserIdDTO toGetUserIdDTO(String userId) {
        return AuthResponseDTO.GetUserIdDTO.builder()
                .userId(userId)
                .build();
    }
}
