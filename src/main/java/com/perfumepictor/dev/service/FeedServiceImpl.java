package com.perfumepictor.dev.service;

import com.perfumepictor.dev.dto.CreateFeedRequestDTO;
import com.perfumepictor.dev.dto.GetFeedResponseDTO;
import com.perfumepictor.dev.entity.Feed;
import com.perfumepictor.dev.entity.Like;
import com.perfumepictor.dev.payload.code.status.ErrorStatus;
import com.perfumepictor.dev.payload.exception.GeneralException;
import com.perfumepictor.dev.repository.FeedRepository;
import com.perfumepictor.dev.repository.LikeRepository;
import com.perfumepictor.dev.util.RedisSortedSetUtil;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.enhanced.dynamodb.Key;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final int ZONE_OFFSET_HOUR = 9;
    private final FeedRepository feedRepository;
    private final LikeRepository likeRepository;
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

        redisSortedSetUtil.addElement("feeds", feed.getKey(), feed.getCreatedAt().toEpochSecond(ZoneOffset.ofHours(ZONE_OFFSET_HOUR)));
        System.out.println(redisSortedSetUtil.getElements("feeds"));
        return feed;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetFeedResponseDTO> getFeeds(int page, int size) {
        String userId = authService.getCurrentUserId();
        Set<String> keys = redisSortedSetUtil.getElementsReverse("feeds", page * size, (page + 1) * size - 1);
        List<Feed> feeds = feedRepository.getFeeds(keys);
        List<Like> likes = likeRepository.getLikes(userId, keys);

        Map<String, Boolean> likeMap = likes.stream()
                .collect(Collectors.toMap(Like::getFeedKey, Like::isLike));

        List<GetFeedResponseDTO> feedResponseDTOs = new java.util.ArrayList<>(feeds.stream()
                .map(feed -> GetFeedResponseDTO.from(feed,
                        likeMap.getOrDefault(feed.getKey(), false)))
                .toList());

        feedResponseDTOs.sort(Comparator.comparing(GetFeedResponseDTO::createdAt).reversed());

        // TODO: 캐시 미스 났을 때 -> 걍 인기순으로 할까?
        return feedResponseDTOs;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getUserFeeds(String userId, String lastFeedKey, int size) {
        Map<String, Object> feedPage = feedRepository.getFeedsByUserId(userId, lastFeedKey, size);
        List<Feed> feeds = (List<Feed>) feedPage.get("feeds");
        Set<String> feedKeys = feeds.stream()
                .map(Feed::getKey)
                .collect(Collectors.toSet());

        List<Like> likes = likeRepository.getLikes(userId, feedKeys);
        Map<String, Boolean> likeMap = likes.stream()
                .collect(Collectors.toMap(Like::getFeedKey, Like::isLike));

        List<GetFeedResponseDTO> feedResponseDTOs = feeds.stream()
                .map(feed -> GetFeedResponseDTO.from(feed, likeMap.getOrDefault(feed.getKey(), false)))
                .toList();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("feedDTOs", feedResponseDTOs);
        resultMap.put("lastFeedKey", feedPage.get("lastFeedKey"));
        return resultMap;
    }

    @Override
    @Transactional
    public Long deleteFeed(String feedKey) {
        feedRepository.deleteFeed(feedKey);
        return redisSortedSetUtil.removeElement("feeds", feedKey);
    }

    @Override
    @Transactional
    public Like likeFeed(String feedKey) {
        String userId = authService.getCurrentUserId();

        Like like = likeRepository.getLike(userId, feedKey).orElse(Like.builder()
                        .userId(userId)
                        .feedKey(feedKey)
                        .like(false)
                        .build());
        Feed feed = feedRepository.getFeed(feedKey)
                .orElseThrow(() -> new GeneralException(ErrorStatus.FEED_NOT_FOUND));

        like.toggleLike();
        feed.updateLikeCount(like.isLike());
        likeRepository.updateLike(like);
        feedRepository.updateFeed(feed);
        return like;
    }
}
