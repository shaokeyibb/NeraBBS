<script lang="ts" setup>
import MasonryWall from "@yeger/vue-masonry-wall";
import NCard from "../../components/NCard.vue";
import NCardHeader from "../../components/NCardHeader.vue";
import NCardMain from "../../components/NCardMain.vue";

import {
  inject,
  onMounted,
  onUnmounted,
  ref,
  useTemplateRef,
  watch,
} from "vue";
import type { PreviewPost } from "../../types/backend.ts";
import { useTimeAgoLocalized } from "../../utils/time.ts";
import { useInfiniteScroll } from "@vueuse/core";
import useBackend from "../../hooks/backend.ts";
import { layout } from "../../utils/symbol.ts";
import { useI18n } from "vue-i18n";
import type { ErrorMessage } from "../../types/error-handling.ts";
import NText from "../../components/NText.vue";
import { snackbar } from "mdui";

const { t } = useI18n();
const { getPreviewPost, getUserProfile } = useBackend();

const wallContainer = useTemplateRef<HTMLDivElement>("wallContainer");

const { page, loading, error, reachedEnd } = {
  page: ref(0),
  loading: ref(false),
  error: ref<string | undefined>(),
  reachedEnd: ref(false),
};
const items = ref<PreviewPost[]>([]);

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
        error.value = (e as ErrorMessage).message;
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
    snackbar({
      message: newError,
      closeable: true,
      queue: "error",
      autoCloseDelay: 3000,
    });
  }
});

onUnmounted(() => {
  items.value = [];
  infiniteScroll.reset();
});

const l = inject(layout);

const topAppBarHeight = (l?.topAppBar?.height ?? 0) + "px";
</script>

<template>
  <div ref="wallContainer" class="wallContainer">
    <MasonryWall
      ref="wall"
      :columnWidth="282"
      :gap="8"
      :items="items"
      :scroll-container="wallContainer"
      class="wall"
    >
      <template #default="{ item }">
        <NCard clickable>
          <template #header>
            <NCardHeader
              :header="
                getUserProfile(item.posterID).value?.username ?? t('loading')
              "
              :subhead="useTimeAgoLocalized(item.createAt).value"
            >
              <template #monogram>
                <mdui-avatar
                  :src="getUserProfile(item.posterID).value?.avatarPath"
                >
                  {{
                    (getUserProfile(item.posterID).value?.username ?? "")[0] ??
                    ""
                  }}
                </mdui-avatar>
              </template>
            </NCardHeader>
          </template>
          <NCardMain :supporting-text="item.content" :title="item.title" />
        </NCard>
      </template>
    </MasonryWall>
    <div v-if="reachedEnd" class="reachedEnd">
      <mdui-divider class="reachedEnd--divider" />
      <NText>
        {{ t("page.explore.reachEnd") }}
      </NText>
    </div>
  </div>
</template>

<style scoped>
.wallContainer {
  display: flex;
  flex-direction: column;

  box-sizing: border-box;
  max-height: calc(
    100vh - v-bind(topAppBarHeight)
  ); /* 64px is the height of the top app bar */
  overflow-y: auto;
  padding: 16px 24px;
}

.reachedEnd {
  width: 30%;

  align-self: center;

  text-align: center;
}

.reachedEnd--divider {
  margin: 12px 0;
}
</style>
