package com.perfumepictor.dev.service;


import com.perfumepictor.dev.dto.CreateFeedRequestDTO;
import com.perfumepictor.dev.entity.Feed;

public interface FeedsService {

    Feed createFeed(CreateFeedRequestDTO requestDTO);
}
