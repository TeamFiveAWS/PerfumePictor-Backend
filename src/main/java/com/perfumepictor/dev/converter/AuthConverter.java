package com.perfumepictor.dev.converter;

import com.perfumepictor.dev.dto.AuthResponseDTO;

public class AuthConverter {

    public static AuthResponseDTO.GetUserIdDTO toGetUserIdDTO(String userId){
        return AuthResponseDTO.GetUserIdDTO.builder()
                .userId(userId)
                .build();
    }
}
