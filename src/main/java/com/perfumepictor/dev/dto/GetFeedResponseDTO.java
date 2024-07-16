package com.perfumepictor.dev.dto;

import com.perfumepictor.dev.entity.Feed;

public record GetFeedResponseDTO(
        String key,
        String userId,
        String profileImg,
        String content,
        String contentImg,
        String perfumeInfo,
        String createdAt,
        Integer likeCount,
        boolean liked) {

    public static GetFeedResponseDTO from(Feed feed, boolean liked) {
        return new GetFeedResponseDTO(
                feed.getKey(),
                feed.getUserId(),
                feed.getProfileImg(),
                feed.getContent(),
                feed.getContentImg(),
                feed.getPerfumeInfo(),
                feed.getCreatedAt().toString(),
                feed.getLikeCount(),
                liked
        );
    }
}
