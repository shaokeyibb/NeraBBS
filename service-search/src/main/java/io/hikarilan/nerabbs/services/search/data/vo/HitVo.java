package io.hikarilan.nerabbs.services.search.data.vo;

import io.hikarilan.nerabbs.services.search.database.entity.HitEntity;
import jakarta.validation.constraints.NotNull;

public record HitVo(@NotNull String topic, @NotNull String key, long hitCount) {

    @NotNull
    public static HitVo fromHitEntity(@NotNull HitEntity hitEntity) {
        return new HitVo(
                hitEntity.getTopic(),
                hitEntity.getKey(),
                hitEntity.getHitCount()
        );
    }

}
