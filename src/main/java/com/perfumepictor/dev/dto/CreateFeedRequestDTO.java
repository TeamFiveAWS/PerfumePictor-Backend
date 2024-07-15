package com.perfumepictor.dev.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateFeedRequestDTO(
        @NotBlank
        String profileImg,

        @NotBlank
        String content,

        @NotBlank
        String contentImg,

        @NotBlank
        String perfumeInfo) {
}
