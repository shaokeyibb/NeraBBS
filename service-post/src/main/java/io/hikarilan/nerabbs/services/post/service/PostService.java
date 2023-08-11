package io.hikarilan.nerabbs.services.post.service;

import io.hikarilan.nerabbs.services.post.data.vo.PostVo;
import io.hikarilan.nerabbs.services.post.database.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    @Cacheable(value = "post", key = "#id")
    public PostVo getPostByID(long id) {
        return postRepository.findById(id).map(PostVo::fromPostEntity).orElseThrow();
    }

}
