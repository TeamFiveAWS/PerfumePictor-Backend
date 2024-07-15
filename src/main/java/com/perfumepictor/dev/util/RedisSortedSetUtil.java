package com.perfumepictor.dev.util;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisSortedSetUtil {

    private final StringRedisTemplate redisTemplate;
    private final ZSetOperations<String, String> zSetOps;

    @Autowired
    public RedisSortedSetUtil(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.zSetOps = redisTemplate.opsForZSet();
    }


    public void addElement(String key, String value, double score) {
        zSetOps.add(key, value, score);
    }

    public Set<String> getElements(String key) {
        return zSetOps.range(key, 0, -1);
    }

    public Set<String> getElements(String key, long start, long end) {
        return zSetOps.range(key, start, end);
    }

    public Set<String> getElementsReverse(String key, long start, long end) {
        return zSetOps.reverseRange(key, start, end);
    }

    public Set<String> getElementsByScore(String key, double minScore, double maxScore) {
        return zSetOps.rangeByScore(key, minScore, maxScore);
    }

    public Long removeElement(String key, String value) {
        return zSetOps.remove(key, value);
    }

    // TODO: ttl은 배치돌면서 지우기..
}
