package io.hikarilan.nerabbs.services.search.job;

import io.hikarilan.nerabbs.common.interfaces.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class TrendingRefreshJob extends QuartzJobBean implements Timed {

    private final RedisTemplate<String, String> redisTemplate;

    private static final DateTimeFormatter ISO_INSTANT = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault());

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.info("Job {} is running", getClass().getSimpleName());

        for (String topic : Objects.requireNonNull(redisTemplate.opsForSet().members("trending"))) {
            String key = "trending:" + topic + ":time";
            var hKeys = redisTemplate.<String, String>opsForHash().keys(key);
            for (String hKey : hKeys) {
                var value = Objects.requireNonNull(redisTemplate.<String, String>opsForHash().get(key, hKey));
                if (ZonedDateTime.parse(value, ISO_INSTANT).isBefore(ZonedDateTime.now())) {
                    redisTemplate.opsForZSet().remove("trending:" + topic, hKey);
                    redisTemplate.opsForHash().delete(key, hKey);
                }
            }
        }

        log.info("Job {} finished", getClass().getSimpleName());
    }

    @Override
    public Duration getDuration() {
        return Duration.ofHours(1);
    }
}
