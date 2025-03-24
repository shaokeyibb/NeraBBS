<script setup lang="ts">
import type { CommentChain } from "../types/backend.ts";
import NComment from "./NComment.vue";
import { layout } from "../utils/symbol.ts";
import { computed, inject } from "vue";
import type { Layout } from "../types/layout.ts";

const { theCommentChain, depth = 0 } = defineProps<{
  theCommentChain: CommentChain;
  depth?: number;
}>();

const emit = defineEmits<{
  action: [name: string, commentId: number];
}>();

const l = inject(layout) as Layout;

const marginLeft = computed(() =>
  l.isLargeScreen.value
    ? `${Math.min(depth, 4) * 2}em`
    : `${Math.min(depth, 1) * 2}em`,
);
</script>

<template>
  <NComment
    :the-comment="theCommentChain.comment"
    :style="{ 'margin-left': marginLeft, width: `calc(100% - ${marginLeft})` }"
    @action="(...parameter) => emit('action', ...parameter)"
  />
  <NCommentChain
    v-for="commentChain in theCommentChain.children"
    :key="commentChain.comment.id"
    :the-comment-chain="commentChain"
    :depth="depth + 1"
    @action="(...parameter) => emit('action', ...parameter)"
  />
</template>

<style scoped></style>
