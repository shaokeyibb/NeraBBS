package io.hikarilan.nerabbs.services.post.database.repository;

import io.hikarilan.nerabbs.services.post.database.entity.PostEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<PostEntity, Long> {
}
