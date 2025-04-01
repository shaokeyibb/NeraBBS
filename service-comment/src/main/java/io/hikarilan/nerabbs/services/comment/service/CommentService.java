package io.hikarilan.nerabbs.services.comment.service;

import io.hikarilan.nerabbs.lib.services.post.grpc.HasPostRequest;
import io.hikarilan.nerabbs.lib.services.post.grpc.PostGrpc;
import io.hikarilan.nerabbs.services.comment.data.bo.CommentCreationBo;
import io.hikarilan.nerabbs.services.comment.data.vo.CommentChainVo;
import io.hikarilan.nerabbs.services.comment.data.vo.CommentVo;
import io.hikarilan.nerabbs.services.comment.database.entity.CommentEntity;
import io.hikarilan.nerabbs.services.comment.database.repository.CommentRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Validated
public class CommentService {

    private final CommentRepository commentRepository;

    @GrpcClient("nerabbs-service-post")
    private PostGrpc.PostBlockingStub postStub;

    @Cacheable(value = "comment", key = "#id")
    public CommentVo getCommentById(long id) {
        return commentRepository.findById(id).map(CommentVo::fromCommentEntity).orElseThrow();
    }

    @CacheEvict(value = "commentChain", key = "#postCreationBo.postID()")
    @Transactional
    public long createComment(@Valid CommentCreationBo postCreationBo) {
        var hasPost = postStub.hasPost(HasPostRequest.newBuilder().setId(postCreationBo.postID()).build());

        if (!hasPost.getHas()) {
            throw new NoSuchElementException("Post not found.");
        }

        var parentComment = postCreationBo.parentCommentID() == null ? null : commentRepository.findById(postCreationBo.parentCommentID()).orElseThrow();
        var rootComment = parentComment == null ? null : parentComment.getRootComment();

        if (parentComment != null && parentComment.getPostID() != postCreationBo.postID()) {
            throw new IllegalArgumentException("Parent comment does not belong to the same post.");
        }

        if (parentComment != null && parentComment.isFrozen()) {
            throw new NoSuchElementException("Parent comment has been deleted.");
        }

        var entity = commentRepository.save(CommentEntity.fromCommentCreationBo(postCreationBo));
        entity.setParentComment(parentComment);
        entity.setRootComment(rootComment == null ? entity : rootComment);

        return entity.getId();
    }

    private CommentChainVo buildCommentChain(@NotNull CommentEntity rootComment) {
        var rootChain = new CommentChainVo(CommentVo.fromCommentEntity(rootComment), new ArrayList<>());
        Map<Long, CommentChainVo> commentChainMap = new HashMap<>();
        commentChainMap.put(rootComment.getId(), rootChain);

        Stack<CommentVo> pendingParents = new Stack<>();
        pendingParents.push(CommentVo.fromCommentEntity(rootComment));

        var allChildren = commentRepository.getAllByRootCommentIdOrderByCreateAt(rootComment.getId()).stream().map(CommentVo::fromCommentEntity).toList();

        while (!pendingParents.isEmpty()) {
            for (int i = 0, length = pendingParents.size(); i < length; i++) {
                var parent = pendingParents.pop();
                var parentChain = commentChainMap.get(parent.id());

                var children = allChildren.stream().filter(it -> Objects.equals(it.parentCommentID(), parent.id())).toList();
                for (var child : children) {
                    var childChain = new CommentChainVo(child, new ArrayList<>());
                    commentChainMap.put(child.id(), childChain);
                    parentChain.children().add(childChain);
                    pendingParents.push(child);
                }
            }
        }

        return rootChain;
    }

    @Cacheable(value = "commentChain", key = "#postID")
    public List<CommentChainVo> getComments(long postID) {
        var hasPost = postStub.hasPost(HasPostRequest.newBuilder().setId(postID).build());

        if (!hasPost.getHas()) {
            throw new NoSuchElementException("Post not found");
        }

        return commentRepository.getAllByPostIDAndParentCommentIsNullOrderByCreateAt(postID).stream().map(this::buildCommentChain).collect(Collectors.toList());
    }

    @Caching(evict = {
            @CacheEvict(value = "comment", key = "#id"),
            @CacheEvict(value = "commentChain", key = "#postID")
    })
    @Transactional
    public void deleteComment(@SuppressWarnings("unused") long postID /* for cache evict */, long id) {
        var chainExists = commentRepository.existsByParentCommentId(id);
        if (chainExists) {
            var entity = commentRepository.findById(id).orElseThrow();
            entity.setFrozen(true);
            commentRepository.save(entity);
            return;
        }
        commentRepository.deleteById(id);
    }

    @Caching(evict = {
            @CacheEvict(value = "comment", allEntries = true),
            @CacheEvict(value = "commentChain", key = "#postID")
    })
    @Transactional
    public void deleteAllComments(long postID) {
        commentRepository.deleteAllByPostID(postID);
    }
}
