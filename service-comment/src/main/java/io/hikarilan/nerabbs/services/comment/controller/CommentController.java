package io.hikarilan.nerabbs.services.comment.controller;

import io.hikarilan.nerabbs.common.BizConstants;
import io.hikarilan.nerabbs.common.exception.ForbiddenException;
import io.hikarilan.nerabbs.common.exception.UnauthorizedException;
import io.hikarilan.nerabbs.services.comment.data.bo.CommentCreationBo;
import io.hikarilan.nerabbs.services.comment.data.dto.CommentCreationDto;
import io.hikarilan.nerabbs.services.comment.data.vo.CommentChainVo;
import io.hikarilan.nerabbs.services.comment.data.vo.CommentVo;
import io.hikarilan.nerabbs.services.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts/{postID}/comments")
@Validated
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{commentID}")
    public CommentVo getComment(@PathVariable long postID, @PathVariable long commentID) {
        var comment = commentService.getCommentById(commentID);

        if (comment.postID() != postID)
            throw new IllegalArgumentException("Comment does not belong to the post.");

        return comment;
    }

    @GetMapping
    public List<CommentChainVo> getComments(@PathVariable long postID) {
        return commentService.getComments(postID);
    }

    @PostMapping
    public long createComment(@RequestHeader(BizConstants.USER_ID_HEADER) long userID, @PathVariable long postID, @RequestBody CommentCreationDto commentCreationDto) {
        if (userID == BizConstants.USER_ID_UNAUTHORIZED)
            throw new UnauthorizedException();

        return commentService.createComment(CommentCreationBo.from(userID, postID, commentCreationDto));
    }

    @DeleteMapping("/{commentID}")
    public void deleteComment(@RequestHeader(BizConstants.USER_ID_HEADER) long userID, @PathVariable long postID, @PathVariable long commentID) {
        if (userID == BizConstants.USER_ID_UNAUTHORIZED)
            throw new UnauthorizedException();

        var comment = commentService.getCommentById(commentID);

        if (comment.commenterID() != userID)
            throw new ForbiddenException();

        if (comment.postID() != postID)
            throw new IllegalArgumentException("Comment does not belong to the post.");

        commentService.deleteComment(postID, commentID);
    }

}
