<script setup lang="ts">
import { useI18n } from "vue-i18n";
import useSearch from "../../hooks/search.ts";
import { computedAsync } from "@vueuse/core";
import NText from "../../components/NText.vue";
import { useRouter } from "vue-router";
import { computed, inject, type Ref } from "vue";
import { layout } from "../../utils/symbol.ts";
import type { Layout } from "../../types/layout.ts";
import usePost from "../../hooks/post.ts";

const { t } = useI18n();

const router = useRouter();

const { getPostTrending } = useSearch();
const { getPost } = usePost();

const trending = computedAsync(async () => await getPostTrending());

const cachedPostTitle: {
  [key: string]: Ref<string | undefined>;
} = {};

const getPostTitleCached = (id: number) => {
  if (cachedPostTitle[id] === undefined) {
    cachedPostTitle[id] = computedAsync(
      async () => (await getPost(id, true)).title,
      "",
    );
  }
  return cachedPostTitle[id];
};

const l = inject(layout) as Layout;

const outContainerMargin = computed(() =>
  l.isLargeScreen.value ? "0 240px" : "0",
);
</script>

<template>
  <mdui-list>
    <mdui-list-item nonclickable>
      <NText scale="headline" size="medium"
        >{{ t("page.trending.title") }}
      </NText>
    </mdui-list-item>
    <mdui-list-item
      v-for="item in trending"
      :key="`${item.topic}:${item.key}`"
      :headline="getPostTitleCached(parseInt(item.key)).value"
      :description="t('page.trending.supporting', { hit: item.hitCount })"
      @click="router.push({ name: 'post-id', params: { id: item.key } })"
    />
  </mdui-list>
</template>

<style scoped>
mdui-list {
  margin: v-bind(outContainerMargin);
}
</style>
