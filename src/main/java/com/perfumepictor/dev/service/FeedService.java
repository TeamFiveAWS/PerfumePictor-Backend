package com.perfumepictor.dev.service;


import com.perfumepictor.dev.dto.CreateFeedRequestDTO;
import com.perfumepictor.dev.entity.Feed;
import com.perfumepictor.dev.entity.Like;
import java.util.List;
import java.util.Map;

public interface FeedService {

    Feed createFeed(CreateFeedRequestDTO requestDTO);
    List<Feed> getFeeds(int page, int size);
    Map<String, Object> getMyFeeds(String userId, String lastFeedKey, int size);
    Long deleteFeed(String feedKey);
    Like likeFeed(String feedKey);
}
