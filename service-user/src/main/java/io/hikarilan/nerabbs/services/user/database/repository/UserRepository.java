package io.hikarilan.nerabbs.services.user.database.repository;

import io.hikarilan.nerabbs.services.user.database.entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    boolean existsByEmail(@NotNull String email);

    Optional<UserEntity> findByEmail(@NotNull String email);

}
