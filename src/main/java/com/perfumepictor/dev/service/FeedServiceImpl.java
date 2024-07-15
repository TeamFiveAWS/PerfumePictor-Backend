package com.perfumepictor.dev.service;

import com.perfumepictor.dev.dto.CreateFeedRequestDTO;
import com.perfumepictor.dev.entity.Feed;
import com.perfumepictor.dev.entity.Like;
import com.perfumepictor.dev.repository.FeedRepository;
import com.perfumepictor.dev.repository.LikeRepository;
import com.perfumepictor.dev.util.RedisSortedSetUtil;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

        // TODO: 캐시 미스 났을 때 -> 걍 인기순으로 할까?
        return feeds;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getMyFeeds(String userId, String lastFeedKey, int size) {
        return feedRepository.getFeedsByUserId(userId, lastFeedKey, size);
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

        Like like = likeRepository.getLike(Key.builder().partitionValue(userId).sortValue(feedKey).build())
                .orElse(Like.builder()
                        .userId(userId)
                        .feedKey(feedKey)
                        .like(false)
                        .build());

        like.toggleLike();
        likeRepository.updateLike(like);
        return like;
    }
}
