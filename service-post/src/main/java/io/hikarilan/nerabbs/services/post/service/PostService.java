package io.hikarilan.nerabbs.services.post.service;

import io.hikarilan.nerabbs.services.post.data.bo.PostCreationBo;
import io.hikarilan.nerabbs.services.post.data.vo.PostVo;
import io.hikarilan.nerabbs.services.post.database.entity.PostEntity;
import io.hikarilan.nerabbs.services.post.database.repository.PostRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Service
@Validated
public class PostService {

    private final PostRepository postRepository;

    @Cacheable(value = "post", key = "#id")
    public PostVo getPostByID(long id) {
        return postRepository.findById(id).map(PostVo::fromPostEntity).orElseThrow();
    }

    public long createPost(@Valid PostCreationBo postCreationBo) {
        return postRepository.save(PostEntity.fromPostCreationBo(postCreationBo)).getId();
    }

}
