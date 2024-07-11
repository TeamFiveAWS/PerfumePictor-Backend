package com.perfumepictor.dev.service;

import com.perfumepictor.dev.dto.CreateFeedRequestDTO;
import com.perfumepictor.dev.entity.Feed;
import com.perfumepictor.dev.repository.FeedRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedsServiceImpl implements FeedsService {

    private final FeedRepository feedRepository;

    @Override
    public Feed createFeed(CreateFeedRequestDTO requestDTO) {

        Feed feed = Feed.builder()
                .userId(requestDTO.userId())
                .profileImg(requestDTO.profileImg())
                .content(requestDTO.content())
                .contentImg(requestDTO.contentImg())
                .perfumeInfo(requestDTO.perfumeInfo())
                .build();

        feedRepository.createFeed(feed);

        return feed;
    }
}
