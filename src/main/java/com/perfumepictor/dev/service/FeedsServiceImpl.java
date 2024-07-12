package com.perfumepictor.dev.service;

import com.perfumepictor.dev.dto.CreateFeedRequestDTO;
import com.perfumepictor.dev.entity.Feed;
import com.perfumepictor.dev.repository.FeedRepository;
import com.perfumepictor.dev.util.RedisSortedSetUtil;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedsServiceImpl implements FeedsService {

    private final int ZONE_OFFSET_HOUR = 9;
    private final FeedRepository feedRepository;
    private final RedisSortedSetUtil redisSortedSetUtil;

    @Override
    @Transactional
    public Feed createFeed(CreateFeedRequestDTO requestDTO) {

        Feed feed = Feed.builder()
                .userId(requestDTO.userId())
                .profileImg(requestDTO.profileImg())
                .content(requestDTO.content())
                .contentImg(requestDTO.contentImg())
                .perfumeInfo(requestDTO.perfumeInfo())
                .build();

        // TODO: 예외처리
        feedRepository.createFeed(feed);

        redisSortedSetUtil.addElement("feeds", feed.getPK() + feed.getSK(), feed.getCreatedAt().toEpochSecond(ZoneOffset.ofHours(ZONE_OFFSET_HOUR)));
        System.out.println(redisSortedSetUtil.getElements("feeds"));
        return feed;
    }
}
