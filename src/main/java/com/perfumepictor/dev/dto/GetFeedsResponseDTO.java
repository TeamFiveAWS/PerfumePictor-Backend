package com.perfumepictor.dev.dto;

import com.perfumepictor.dev.entity.Feed;
import java.util.List;

public record GetFeedsResponseDTO(
        List<GetFeedResponseDTO> feeds
) {

    public static GetFeedsResponseDTO from(List<Feed> feeds) {
        List<GetFeedResponseDTO> feedResponseDTOs = feeds.stream()
                .map(GetFeedResponseDTO::from)
                .toList();
        return new GetFeedsResponseDTO(feedResponseDTOs);
    }
}
