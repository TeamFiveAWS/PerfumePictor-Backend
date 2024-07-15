package com.perfumepictor.dev.service;

import com.perfumepictor.dev.dto.CreateFeedRequestDTO;
import com.perfumepictor.dev.entity.Feed;
import com.perfumepictor.dev.repository.FeedRepository;
import com.perfumepictor.dev.util.RedisSortedSetUtil;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.KeysAndAttributes;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedsServiceImpl implements FeedsService {

    private final int ZONE_OFFSET_HOUR = 9;
    private final FeedRepository feedRepository;
    private final RedisSortedSetUtil redisSortedSetUtil;
    private final AuthService authService;

    @Override
    @Transactional
    public Feed createFeed(CreateFeedRequestDTO requestDTO) {
        String userId = authService.getCurrentUserId();

        Feed feed = Feed.builder()
                .userId(userId)
                .profileImg(requestDTO.profileImg())
                .content(requestDTO.content())
                .contentImg(requestDTO.contentImg())
                .perfumeInfo(requestDTO.perfumeInfo())
                .build();

        // TODO: 예외처리
        feedRepository.createFeed(feed);

        redisSortedSetUtil.addElement("feeds", feed.getPK() + "$" +  feed.getSK(), feed.getCreatedAt().toEpochSecond(ZoneOffset.ofHours(ZONE_OFFSET_HOUR)));
        System.out.println(redisSortedSetUtil.getElements("feeds"));
        return feed;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Feed> getFeeds(int page, int size) {

        Set<String> keys = redisSortedSetUtil.getElementsReverse("feeds", page * size, (page + 1) * size - 1);
        List<Feed> feeds = feedRepository.getFeeds(keys);
        feeds.sort(Comparator.comparing(Feed::getCreatedAt).reversed());

        // TODO: 캐시 미스 났을 때
        return feeds;
    }

    @Override
    @Transactional
    public Long deleteFeed(String feedKey) {
        System.out.println(feedKey);
        feedRepository.deleteFeed(feedKey);
        return redisSortedSetUtil.removeElement("feeds", feedKey);
    }
}
