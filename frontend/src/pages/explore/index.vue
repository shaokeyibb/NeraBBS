<script lang="ts" setup>
import MasonryWall from "@yeger/vue-masonry-wall";
import NCard from "../../components/NCard.vue";
import NCardHeader from "../../components/NCardHeader.vue";
import NCardMain from "../../components/NCardMain.vue";

import type { Ref } from "vue";
import {
  inject,
  onMounted,
  onUnmounted,
  ref,
  shallowRef,
  useTemplateRef,
  watch,
} from "vue";
import type { PreviewPost, UserProfile } from "../../types/backend.ts";
import { useTimeAgoLocalized } from "../../utils/time.ts";
import { computedAsync, useInfiniteScroll } from "@vueuse/core";
import { layout } from "../../utils/symbol.ts";
import type { ErrorMessage } from "../../types/error-handling.ts";
import useErrorHandling from "../../hooks/error-handling.ts";
import usePost from "../../hooks/post.ts";
import useUser from "../../hooks/user.ts";
import type { Layout } from "../../types/layout.ts";
import { useRouter } from "vue-router";
import NUserAvatar from "../../components/NUserAvatar.vue";
import NReachedEndDivider from "../../components/NReachedEndDivider.vue";

const router = useRouter();
const { getPreviewPost } = usePost();
const { getUserProfile } = useUser();
const { handle: handleError } = useErrorHandling();

const wallContainer = useTemplateRef<HTMLDivElement>("wall-container");

const { page, loading, error, reachedEnd } = {
  page: ref(0),
  loading: ref(false),
  error: ref<ErrorMessage | undefined>(),
  reachedEnd: ref(false),
};
const items = shallowRef<PreviewPost[]>([]);

let infiniteScroll: ReturnType<typeof useInfiniteScroll>;

onMounted(() => {
  infiniteScroll = useInfiniteScroll(
    wallContainer.value,
    async () => {
      loading.value = true;

      try {
        const res = await getPreviewPost(page.value, 20);
        if (res.length === 0) {
          reachedEnd.value = true;
          loading.value = false;
          return;
        }
        items.value = items.value.concat(res);
      } catch (e: unknown) {
        error.value = e as ErrorMessage;
        reachedEnd.value = true;
      }

      page.value++;
      loading.value = false;
    },
    {
      distance: 10,
      canLoadMore: () =>
        reachedEnd.value == false &&
        loading.value === false &&
        error.value === undefined,
    },
  );
});

watch(error, (newError) => {
  if (newError !== undefined) {
    handleError(newError);
  }
});

onUnmounted(() => {
  items.value = [];
  infiniteScroll.reset();
});

const l = inject(layout) as Layout;

l.updateLayout({
  fab: {
    icon: "add",
    onClick: () => {
      router.push({ name: "create" });
    },
  },
});

const cachedUserProfile: {
  [key: number]: Ref<UserProfile | undefined>;
} = {};

const getUserProfileCached = (id: number) => {
  if (cachedUserProfile[id] === undefined) {
    cachedUserProfile[id] = computedAsync(
      async () => await getUserProfile(id),
      {
        userID: id,
      },
    );
  }
  return cachedUserProfile[id];
};
</script>

<template>
  <div ref="wall-container" class="wall-container">
    <MasonryWall
      :columnWidth="282"
      :gap="8"
      :items="items"
      :scroll-container="wallContainer"
    >
      <template #default="{ item }">
        <NCard
          clickable
          @click="router.push({ name: 'post-id', params: { id: item.id } })"
        >
          <template #header>
            <NCardHeader
              :header="
                getUserProfileCached(item.posterID).value?.username ??
                item.posterID.toString()
              "
              :subhead="useTimeAgoLocalized(item.createAt).value"
            >
              <template #monogram>
                <n-user-avatar
                  :user="getUserProfileCached(item.posterID).value"
                />
              </template>
            </NCardHeader>
          </template>
          <NCardMain
            :supporting-text="item.content + '...'"
            :title="item.title"
          />
        </NCard>
      </template>
    </MasonryWall>
    <NReachedEndDivider v-if="reachedEnd" />
  </div>
</template>

<style scoped>
.wall-container {
  display: flex;
  flex-direction: column;

  box-sizing: border-box;
  overflow-y: auto;
  padding: 16px 24px;

  user-select: none;
}

.card {
  user-select: none;
}
</style>
