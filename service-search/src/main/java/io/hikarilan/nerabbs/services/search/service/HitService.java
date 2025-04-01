package io.hikarilan.nerabbs.services.search.service;

import io.hikarilan.nerabbs.services.search.data.vo.HitVo;
import io.hikarilan.nerabbs.services.search.database.entity.HitEntity;
import io.hikarilan.nerabbs.services.search.database.repository.HitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class HitService {

    private final HitRepository hitRepository;

    private final TrendingService trendingService;

    @Transactional
    @CacheEvict(value = "hit", key = "#topic+':'+#key")
    public void hit(String topic, String key) {
        HitEntity hit;

        if (!hitRepository.existsById(new HitEntity.HitEntityKey(topic, key))) {
            hit = hitRepository.save(HitEntity.of(topic, key));
        } else {
            hit = hitRepository.getReferenceById(new HitEntity.HitEntityKey(topic, key));
        }

        hit.setHitCount(hit.getHitCount() + 1);

        hitRepository.save(hit);

        trendingService.hit(topic, key);
    }

    @Transactional
    @CacheEvict(value = "hit", key = "#topic+':'+#key")
    public void reset(String topic, String key) {
        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> trendingService.reset(topic, key)),
                CompletableFuture.runAsync(() -> hitRepository.deleteById(new HitEntity.HitEntityKey(topic, key)))).join();
    }

    @Transactional
    @Cacheable(value = "hit", key = "#topic+':'+#key")
    public HitVo getHitCount(String topic, String key) {
        if (!hitRepository.existsById(new HitEntity.HitEntityKey(topic, key))) {
            return new HitVo(topic, key, 0);
        }

        return HitVo.fromHitEntity(hitRepository.getReferenceById(new HitEntity.HitEntityKey(topic, key)));
    }

}
