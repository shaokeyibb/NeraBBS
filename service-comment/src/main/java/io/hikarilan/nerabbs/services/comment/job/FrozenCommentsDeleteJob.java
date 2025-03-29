package io.hikarilan.nerabbs.services.comment.job;

import io.hikarilan.nerabbs.services.comment.database.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FrozenCommentsDeleteJob extends QuartzJobBean {

    public static final int INTERVAL_MINUTE = 1;

    private final CommentRepository commentRepository;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        var frozenComments = commentRepository.getAllByFrozenIsTrue();
        frozenComments = frozenComments.stream().filter(it -> !commentRepository.existsByParentCommentId(it.getId())).toList();
        frozenComments.forEach(it -> it.setDeleted(true));
        commentRepository.saveAll(frozenComments);
    }
}
