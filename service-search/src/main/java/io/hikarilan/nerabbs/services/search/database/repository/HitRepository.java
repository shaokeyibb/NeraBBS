package io.hikarilan.nerabbs.services.search.database.repository;

import io.hikarilan.nerabbs.services.search.database.entity.HitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HitRepository extends JpaRepository<HitEntity, HitEntity.HitEntityKey> {
}
