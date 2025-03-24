package io.hikarilan.nerabbs.services.comment.database.repository;

import io.hikarilan.nerabbs.services.comment.database.entity.CommentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> getAllByRootCommentIdOrderByCreateAt(Long rootCommentId);

    List<CommentEntity> getAllByRootCommentIdOrderByCreateAt(Long rootCommentId, Pageable pageable);

    boolean existsByParentCommentId(Long parentCommentId);

    List<CommentEntity> getAllByFrozenIsTrue();

    List<CommentEntity> getAllByPostIDAndParentCommentIsNullOrderByCreateAt(Long postID);

    List<CommentEntity> getAllByPostIDAndParentCommentIsNullOrderByCreateAt(Long postID, Pageable pageable);

    void deleteAllByPostID(Long postID);

}
