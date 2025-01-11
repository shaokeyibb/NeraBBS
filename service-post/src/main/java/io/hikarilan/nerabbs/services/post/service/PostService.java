package io.hikarilan.nerabbs.services.post.service;

import io.hikarilan.nerabbs.services.post.data.bo.PostCreationBo;
import io.hikarilan.nerabbs.services.post.data.vo.PostVo;
import io.hikarilan.nerabbs.services.post.data.vo.PreviewPostVo;
import io.hikarilan.nerabbs.services.post.database.entity.PostEntity;
import io.hikarilan.nerabbs.services.post.database.repository.PostRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(value = "post", key = "#id")
    public PostVo getPostByID(long id) {
        return postRepository.findById(id).map(PostVo::fromPostEntity).orElseThrow();
    }


    @CacheEvict(value = "previewPosts", allEntries = true)
    @Transactional
    public long createPost(@Valid PostCreationBo postCreationBo) {
        return postRepository.save(PostEntity.fromPostCreationBo(postCreationBo)).getId();
    }

    @Cacheable(value = "previewPosts", key = "#page + '-' + #size")
    public List<PreviewPostVo> getPreviewPosts(int page, int size) {
        return postRepository.findAll(PageRequest.of(page, size, Sort.by("createAt").descending())).map(PreviewPostVo::fromPostEntity).toList();
    }

}
