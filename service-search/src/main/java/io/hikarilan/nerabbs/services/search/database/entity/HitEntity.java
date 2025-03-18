package io.hikarilan.nerabbs.services.search.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hits")
@SQLDelete(sql = "UPDATE hits SET is_deleted = true WHERE key=? and topic=?")
@SQLRestriction("is_deleted = false")
@IdClass(HitEntity.HitEntityKey.class)
@Data
public class HitEntity {

    @Id
    @Column(nullable = false)
    @Getter
    @Setter
    private String topic;

    @Id
    @Column(nullable = false)
    @Getter
    @Setter
    private String key;

    @Column(nullable = false)
    @Getter
    @Setter
    private Long hitCount;

    @ColumnDefault("false")
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public static HitEntity of(String topic, String key) {
        return new HitEntity(topic, key, 0L, false);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class HitEntityKey implements Serializable {
        private String topic;
        private String key;
    }

}
