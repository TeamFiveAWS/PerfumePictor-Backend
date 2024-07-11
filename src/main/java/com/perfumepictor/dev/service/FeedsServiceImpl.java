package com.perfumepictor.dev.service;

import com.perfumepictor.dev.dto.FeedRequest;
import com.perfumepictor.dev.entity.Feeds;
import com.perfumepictor.dev.repository.FeedRepository;
import com.perfumepictor.dev.utils.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedsServiceImpl implements FeedsService {

    private final FeedRepository feedRepository;

    @Override
    public Feeds createFeed(FeedRequest feedRequest) {

        Feeds feed = Feeds.builder()
                .email(feedRequest.email())
                .nickname(feedRequest.nickname())
                .imageUrl(feedRequest.imageUrl())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();

        feedRepository.createFeed(feed);

        return feed;
    }
}
