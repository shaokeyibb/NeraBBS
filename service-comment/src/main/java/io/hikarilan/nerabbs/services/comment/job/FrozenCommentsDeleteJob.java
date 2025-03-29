package io.hikarilan.nerabbs.services.comment.job;

import io.hikarilan.nerabbs.common.interfaces.Timed;
import io.hikarilan.nerabbs.services.comment.database.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class FrozenCommentsDeleteJob extends QuartzJobBean implements Timed {

    private final CommentRepository commentRepository;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        var frozenComments = commentRepository.getAllByFrozenIsTrue();
        frozenComments = frozenComments.stream().filter(it -> !commentRepository.existsByParentCommentId(it.getId())).toList();
        frozenComments.forEach(it -> it.setDeleted(true));
        commentRepository.saveAll(frozenComments);
    }

    @Override
    public Duration getDuration() {
        return Duration.ofHours(1);
    }
}
