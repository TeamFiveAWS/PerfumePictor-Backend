package com.perfumepictor.dev.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record FeedRequest(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String nickname,

        @NotBlank
        String imageUrl) {
}
