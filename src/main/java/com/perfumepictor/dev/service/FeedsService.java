package com.perfumepictor.dev.service;


import com.perfumepictor.dev.dto.FeedRequest;
import com.perfumepictor.dev.entity.Feeds;

public interface FeedsService {

    Feeds createFeed(FeedRequest feedRequest);
}
