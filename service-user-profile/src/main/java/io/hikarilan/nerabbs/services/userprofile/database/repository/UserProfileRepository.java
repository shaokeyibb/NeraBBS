package io.hikarilan.nerabbs.services.userprofile.database.repository;

import io.hikarilan.nerabbs.services.userprofile.database.entity.UserProfileEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends CrudRepository<UserProfileEntity, Long> {

}
