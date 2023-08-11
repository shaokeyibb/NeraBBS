package io.hikarilan.nerabbs.services.post.controller;

import io.hikarilan.nerabbs.services.post.data.vo.PostVo;
import io.hikarilan.nerabbs.services.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
@Validated
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    @ResponseBody
    public PostVo getPost(@PathVariable long id) {
        return postService.getPostByID(id);
    }

}
