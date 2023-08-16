package io.hikarilan.nerabbs.services.post.data.bo;

import io.hikarilan.nerabbs.services.post.data.dto.PostCreationDto;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public record PostCreationBo(
        long userId,
        String title,
        String content
) {

    public static PostCreationBo from(long userId, @Valid PostCreationDto postCreationDto) {
        return new PostCreationBo(userId, postCreationDto.title(), postCreationDto.content());
    }

}
