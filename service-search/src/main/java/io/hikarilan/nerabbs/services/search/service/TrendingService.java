package io.hikarilan.nerabbs.services.search.service;

import io.hikarilan.nerabbs.services.search.data.vo.HitVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class TrendingService {

    private final RedisTemplate<String, String> redisTemplate;

    public void hit(String topic, String key) {
        redisTemplate.opsForSet().add("trending", topic);
        redisTemplate.opsForZSet().incrementScore("trending:" + topic, key, 1);
        redisTemplate.<String, String>opsForHash().put("trending:" + topic + ":time", key, ZonedDateTime.now().plusHours(1).toInstant().toString());
    }

    public void reset(String topic, String key) {
        redisTemplate.opsForZSet().remove("trending:" + topic, key);
    }

    public List<HitVo> getTrending(String topic, int limit) {
        return Objects.requireNonNull(redisTemplate.opsForZSet().reverseRangeWithScores("trending:" + topic, 0, limit))
                .stream().map(it -> new HitVo(topic, Objects.requireNonNull(it.getValue()), Objects.requireNonNull(it.getScore()).longValue())).toList();
    }

}
