package io.hikarilan.nerabbs.services.post.controller;

import io.hikarilan.nerabbs.common.BizConstants;
import io.hikarilan.nerabbs.common.exception.ForbiddenException;
import io.hikarilan.nerabbs.common.exception.UnauthorizedException;
import io.hikarilan.nerabbs.services.post.data.bo.PostCreationBo;
import io.hikarilan.nerabbs.services.post.data.dto.PostCreationDto;
import io.hikarilan.nerabbs.services.post.data.vo.PostVo;
import io.hikarilan.nerabbs.services.post.data.vo.PreviewPostVo;
import io.hikarilan.nerabbs.services.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    public long createPost(@RequestHeader(BizConstants.USER_ID_HEADER) long userID, @RequestBody @Valid PostCreationDto postCreationDto) {
        if (userID == BizConstants.USER_ID_UNAUTHORIZED)
            throw new UnauthorizedException();

        return postService.createPost(PostCreationBo.from(userID, postCreationDto));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void deletePost(@RequestHeader(BizConstants.USER_ID_HEADER) long userID, @PathVariable long id) {
        if (userID == BizConstants.USER_ID_UNAUTHORIZED)
            throw new UnauthorizedException();

        var post = postService.getPostByID(id);

        if (post.posterID() != userID)
            throw new ForbiddenException();

        postService.deletePost(id);
    }

    @GetMapping
    @ResponseBody
    public List<PreviewPostVo> getPreviewPosts(@RequestParam int page, @RequestParam int size) {
        return postService.getPreviewPosts(page, size);
    }
}
