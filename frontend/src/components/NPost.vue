<script setup lang="ts">
import { VueMarkdownIt } from "@f3ve/vue-markdown-it";
import { useTimeAgoLocalized } from "../utils/time.ts";
import type { Post } from "../types/backend.ts";
import useUser from "../hooks/user.ts";
import { computedAsync } from "@vueuse/core";
import useSearch from "../hooks/search.ts";
import NUserAvatar from "./NUserAvatar.vue";
import NText from "./NText.vue";

const { thePost, isPreview } = defineProps<{
  thePost: Post;
  isPreview?: boolean;
}>();

const { getUserProfile } = useUser();
const { getPostHitCount } = useSearch();

const { user, hit } = {
  user: isPreview
    ? null
    : computedAsync(async () => await getUserProfile(thePost.posterID)),
  hit: isPreview
    ? null
    : computedAsync(
        async () => (await getPostHitCount("" + thePost.id)).hitCount,
      ),
};
</script>

<template>
  <NText as="h1" scale="title" size="large">{{ thePost.title }}</NText>
  <div v-if="!isPreview" class="metadata">
    <div class="inline-metadata">
      <mdui-chip icon="access_time">
        <NText>{{ useTimeAgoLocalized(thePost.createAt) }}</NText>
      </mdui-chip>
      <mdui-chip icon="remove_red_eye">
        <NText>{{ hit }}</NText>
      </mdui-chip>
    </div>
    <div class="author" v-if="user">
      <NUserAvatar :user="user" />
      <div class="author-text">
        <NText>{{ user.username }}</NText>
        <NText scale="body" size="small">{{ user.signature }}</NText>
      </div>
    </div>
  </div>
  <div class="mdui-prose">
    <vue-markdown-it :source="thePost.content" />
  </div>
</template>

<style scoped>
h1 {
  font-size: 2em;
  margin-bottom: 0.5em;
}

.metadata {
  display: flex;
  flex-direction: column;

  margin-bottom: 1em;

  gap: 1em;
}

.inline-metadata {
  display: flex;
  gap: 1em;
  flex-wrap: wrap;
}

.author {
  display: flex;
  align-items: center;
  gap: 1em;

  user-select: none;
}

.author-text {
  display: flex;
  flex-direction: column;

  gap: 2px;
}
</style>
