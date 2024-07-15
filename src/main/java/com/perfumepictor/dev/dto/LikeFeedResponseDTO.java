package com.perfumepictor.dev.dto;

import com.perfumepictor.dev.entity.Like;
import java.time.LocalDateTime;

public record LikeFeedResponseDTO(
        boolean like,
        LocalDateTime updatedAt
) {
    public static LikeFeedResponseDTO from(Like like) {
        return new LikeFeedResponseDTO(like.isLike(), like.getUpdatedAt());
    }
}
