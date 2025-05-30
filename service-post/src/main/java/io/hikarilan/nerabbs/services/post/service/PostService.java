package io.hikarilan.nerabbs.services.post.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hikarilan.nerabbs.lib.services.comment.grpc.CommentGrpc;
import io.hikarilan.nerabbs.lib.services.comment.grpc.DeleteAllCommentsRequest;
import io.hikarilan.nerabbs.lib.services.search.grpc.*;
import io.hikarilan.nerabbs.services.post.data.bo.PostCreationBo;
import io.hikarilan.nerabbs.services.post.data.vo.PostVo;
import io.hikarilan.nerabbs.services.post.data.vo.PreviewPostVo;
import io.hikarilan.nerabbs.services.post.database.entity.PostEntity;
import io.hikarilan.nerabbs.services.post.database.repository.PostRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RequiredArgsConstructor
@Service
@Validated
public class PostService {

    private final PostRepository postRepository;

    @GrpcClient("nerabbs-service-search")
    private SearchGrpc.SearchFutureStub searchStub;

    @GrpcClient("nerabbs-service-search")
    private HitGrpc.HitFutureStub hitStub;

    @GrpcClient("nerabbs-service-comment")
    private CommentGrpc.CommentFutureStub commentStub;

    private final ObjectMapper objectMapper;

    @Cacheable(value = "post", key = "#id")
    public PostVo getPostByID(long id) {
        return postRepository.findById(id).map(PostVo::fromPostEntity).orElseThrow();
    }

    @Cacheable(value = "hasPost", key = "#id")
    public boolean hasPost(long id) {
        return postRepository.existsById(id);
    }

    @SneakyThrows
    @CacheEvict(value = "previewPosts", allEntries = true)
    @GlobalTransactional
    @Transactional
    public long createPost(@Valid PostCreationBo postCreationBo) {
        var entity = postRepository.save(PostEntity.fromPostCreationBo(postCreationBo));

        searchStub.addDocuments(AddDocumentsRequest.newBuilder().setIndex("posts").setPrimaryKey("id").addDocument(objectMapper.writeValueAsString(entity)).build());

        return entity.getId();
    }

    @Cacheable(value = "previewPosts", key = "#page + '-' + #size")
    public List<PreviewPostVo> getPreviewPosts(int page, int size) {
        return postRepository.findAll(PageRequest.of(page, size, Sort.by("createAt").descending())).map(PreviewPostVo::fromPostEntity).toList();
    }

    @Caching(evict = {
            @CacheEvict(value = "post", key = "#id"),
            @CacheEvict(value = "previewPosts", allEntries = true),
            @CacheEvict(value = "hasPost", key = "#id")
    })
    @GlobalTransactional
    public void deletePost(long id) {
        searchStub.deleteDocuments(DeleteDocumentsRequest.newBuilder().setIndex("posts").addPrimaryKey(String.valueOf(id)).build());
        hitStub.reset(ResetRequest.newBuilder().setTopic("posts").setKey(String.valueOf(id)).build());
        commentStub.deleteAllComments(DeleteAllCommentsRequest.newBuilder().setPostId(id).build());

        postRepository.deleteById(id);
    }

}
