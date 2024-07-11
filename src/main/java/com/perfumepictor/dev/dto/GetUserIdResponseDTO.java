package com.perfumepictor.dev.dto;

public record GetUserIdResponseDTO(
        String userId) {

    public static GetUserIdResponseDTO from(String userId) {
        return new GetUserIdResponseDTO(userId);
    }
}