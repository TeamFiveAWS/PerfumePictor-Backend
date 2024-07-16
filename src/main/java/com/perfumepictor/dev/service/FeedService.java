package com.perfumepictor.dev.service;


import com.perfumepictor.dev.dto.CreateFeedRequestDTO;
import com.perfumepictor.dev.dto.GetFeedResponseDTO;
import com.perfumepictor.dev.entity.Feed;
import com.perfumepictor.dev.entity.Like;
import java.util.List;
import java.util.Map;

public interface FeedService {

    Feed createFeed(CreateFeedRequestDTO requestDTO);
    List<GetFeedResponseDTO> getFeeds(int page, int size);
    Map<String, Object> getUserFeeds(String userId, String lastFeedKey, int size);
    Long deleteFeed(String feedKey);
    Like likeFeed(String feedKey);
}
