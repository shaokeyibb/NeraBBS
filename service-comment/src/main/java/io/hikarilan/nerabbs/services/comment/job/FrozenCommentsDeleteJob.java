package io.hikarilan.nerabbs.services.comment.job;

import io.hikarilan.nerabbs.common.interfaces.Timed;
import io.hikarilan.nerabbs.services.comment.database.entity.CommentEntity;
import io.hikarilan.nerabbs.services.comment.database.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class FrozenCommentsDeleteJob extends QuartzJobBean implements Timed {

    private final CommentRepository commentRepository;

    private final CacheManager cacheManager;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.info("Job {} is running", getClass().getSimpleName());

        var frozenComments = commentRepository.getAllByFrozenIsTrue();
        frozenComments = frozenComments.stream().filter(it -> !commentRepository.existsByParentCommentId(it.getId())).toList();
        frozenComments.forEach(it -> it.setDeleted(true));
        commentRepository.saveAll(frozenComments);

        frozenComments.stream().map(CommentEntity::getId).distinct().forEach(it -> Objects.requireNonNull(cacheManager.getCache("comment")).evict(it));
        frozenComments.stream().map(CommentEntity::getPostID).distinct().forEach(it -> Objects.requireNonNull(cacheManager.getCache("commentChain")).evict(it));

        log.info("Job {} finished, {} comments evicted", getClass().getSimpleName(), frozenComments.size());
    }

    @Override
    public Duration getDuration() {
        return Duration.ofHours(1);
    }
}
