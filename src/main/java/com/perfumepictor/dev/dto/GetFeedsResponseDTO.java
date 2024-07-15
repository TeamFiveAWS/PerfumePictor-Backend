package com.perfumepictor.dev.dto;

import com.perfumepictor.dev.entity.Feed;
import java.util.List;

public record GetFeedsResponseDTO(
        List<GetFeedResponseDTO> feeds,
        String lastFeedKey
) {

    public static GetFeedsResponseDTO from(List<Feed> feeds) {
        List<GetFeedResponseDTO> feedResponseDTOs = feeds.stream()
                .map(GetFeedResponseDTO::from)
                .toList();
        return new GetFeedsResponseDTO(feedResponseDTOs, null);
    }

    public static GetFeedsResponseDTO from(List<Feed> feeds, String lastFeedKey) {
        List<GetFeedResponseDTO> feedResponseDTOs = feeds.stream()
                .map(GetFeedResponseDTO::from)
                .toList();
        return new GetFeedsResponseDTO(feedResponseDTOs, lastFeedKey);
    }
}
