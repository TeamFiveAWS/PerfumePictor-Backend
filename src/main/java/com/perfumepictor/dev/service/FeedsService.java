package com.perfumepictor.dev.service;


import com.perfumepictor.dev.dto.CreateFeedRequestDTO;
import com.perfumepictor.dev.entity.Feed;
import java.util.List;

public interface FeedsService {

    Feed createFeed(CreateFeedRequestDTO requestDTO);
    List<Feed> getFeeds(int page, int size);
}
