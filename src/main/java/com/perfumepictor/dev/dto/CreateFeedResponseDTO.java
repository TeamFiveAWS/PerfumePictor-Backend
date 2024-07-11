package com.perfumepictor.dev.dto;

import com.perfumepictor.dev.entity.Feed;

public record CreateFeedResponseDTO(
        String userId,
        String profileImg,
        String content,
        String contentImg,
        String perfumeInfo,
        String createdAt) {

    public static CreateFeedResponseDTO from(Feed feed) {
        return new CreateFeedResponseDTO(
                feed.getUserId(),
                feed.getProfileImg(),
                feed.getContent(),
                feed.getContentImg(),
                feed.getPerfumeInfo(),
                feed.getCreatedAt().toString()
        );
    }
}
