package io.hikarilan.nerabbs.services.comment.data.vo;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CommentChainVo(
        @NotNull CommentVo comment,
        @NotNull List<@NotNull CommentChainVo> children
) {
}
