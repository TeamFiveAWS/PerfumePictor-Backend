package com.perfumepictor.dev.dto;

import com.perfumepictor.dev.entity.Feed;
import java.util.List;

public record GetFeedsResponseDTO(
        List<GetFeedResponseDTO> feeds,
        String lastFeedKey
) {

    public static GetFeedsResponseDTO from(List<GetFeedResponseDTO> feedDTOs) {
        return new GetFeedsResponseDTO(feedDTOs, null);
    }

    public static GetFeedsResponseDTO from(List<GetFeedResponseDTO> feedDTOs, String lastFeedKey) {
        return new GetFeedsResponseDTO(feedDTOs, lastFeedKey);
    }
}
