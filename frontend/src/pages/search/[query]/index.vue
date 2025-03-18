<script setup lang="ts">
import { useHead } from "@unhead/vue";
import {
  computed,
  inject,
  onMounted,
  type Ref,
  ref,
  shallowRef,
  toValue,
  useTemplateRef,
  watch,
} from "vue";
import { useRoute, useRouter } from "vue-router";
import { useI18n } from "vue-i18n";
import { useTimeAgoLocalized } from "../../../utils/time.ts";
import NCard from "../../../components/NCard.vue";
import NUserAvatar from "../../../components/NUserAvatar.vue";
import NCardHeader from "../../../components/NCardHeader.vue";
import NText from "../../../components/NText.vue";
import NCardMain from "../../../components/NCardMain.vue";
import MasonryWall from "@yeger/vue-masonry-wall";
import { layout } from "../../../utils/symbol.ts";
import type { Layout } from "../../../types/layout.ts";
import type { ErrorMessage } from "../../../types/error-handling.ts";
import type { Post, UserProfile } from "../../../types/backend.ts";
import { computedAsync, useInfiniteScroll } from "@vueuse/core";
import useErrorHandling from "../../../hooks/error-handling.ts";
import useSearch from "../../../hooks/search.ts";
import useUser from "../../../hooks/user.ts";

const router = useRouter();
const route = useRoute();
const { t } = useI18n();
const { getUserProfile } = useUser();
const { searchPost } = useSearch();

const { handle: handleError } = useErrorHandling();

const query = computed(() => {
  return route.params.query as string;
});

const wallContainer = useTemplateRef<HTMLDivElement>("wall-container");

const { page, loading, error, reachedEnd } = {
  page: ref(0),
  loading: ref(false),
  error: ref<ErrorMessage | undefined>(),
  reachedEnd: ref(false),
};
const items = shallowRef<Post[]>([]);

let infiniteScroll: ReturnType<typeof useInfiniteScroll>;

onMounted(() => {
  infiniteScroll = useInfiniteScroll(
    wallContainer.value,
    async () => {
      loading.value = true;

      try {
        let res = await searchPost(query.value, page.value);
        if (res.length === 0) {
          reachedEnd.value = true;
          loading.value = false;
          return;
        }

        const encoder = new TextEncoder();
        const decoder = new TextDecoder();

        const handleMatchesPosition = (
          hit: (typeof res)[0],
          attribute: keyof Post,
        ): number => {
          if (
            !(
              hit[attribute] &&
              hit._matchesPosition[attribute] &&
              hit._matchesPosition[attribute].length > 0
            )
          ) {
            return 0;
          }

          let offset = 0;
          const originByteArr = encoder.encode(hit[attribute] as string);
          for (let position of hit._matchesPosition[attribute]) {
            const byteArr = encoder.encode(hit[attribute] as string);
            const newByteArr = new Uint8Array(byteArr.length + 9);

            // @see: https://github.com/meilisearch/meilisearch/issues/5429
            position.length = encoder.encode(
              decoder
                .decode(
                  encoder
                    .encode(hit[attribute] as string)
                    .slice(offset + position.start),
                )
                .substring(0, position.length),
            ).length;

            newByteArr.set(byteArr.slice(0, offset + position.start), 0);

            newByteArr.set(encoder.encode("<em>"), offset + position.start);
            newByteArr.set(
              originByteArr.slice(
                position.start,
                position.start + position.length,
              ),
              offset + position.start + 4,
            );
            newByteArr.set(
              encoder.encode("</em>"),
              offset + position.start + 4 + position.length,
            );

            newByteArr.set(
              byteArr.slice(offset + position.start + position.length),
              offset + position.start + position.length + 9,
            );

            offset += 9; // <em></em>
            hit[attribute] = decoder.decode(newByteArr) as never;
          }

          return offset;
        };

        const maxContentLength = 200;

        res = res.map((it) => {
          handleMatchesPosition(it, "title");
          const contentOffset = handleMatchesPosition(it, "content");
          if (it.content.length > maxContentLength) {
            if (it._matchesPosition.content) {
              const closestMatches = it._matchesPosition.content.sort((a, b) =>
                Math.abs(
                  b.start +
                    b.length -
                    maxContentLength -
                    (a.start + a.length - maxContentLength),
                ),
              )[0];
              it.content = decoder.decode(
                encoder
                  .encode(it.content)
                  .slice(
                    0,
                    contentOffset +
                      closestMatches.start +
                      closestMatches.length,
                  ),
              );
            } else {
              it.content = it.content.substring(0, maxContentLength);
            }
          }

          return it;
        });
        items.value = items.value.concat(res);
      } catch (e: unknown) {
        console.error(e);
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
  if (newError?.code === 404) return;
  if (newError !== undefined) {
    handleError(newError);
  }
});

watch(query, () => {
  items.value = [];
  infiniteScroll.reset();
  page.value = 0;
  reachedEnd.value = false;
});

const l = inject(layout) as Layout;

const topAppBarHeight = computed(
  () => (toValue(l?.topAppBar)?.height ?? 0) + "px",
);

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

useHead({
  title: computed(() => t("page.search.title", { query: query.value })),
});
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
          <NCardMain :supporting-text="item.content" :title="item.title" />
        </NCard>
      </template>
    </MasonryWall>
    <div v-if="reachedEnd" class="reached-end">
      <mdui-divider class="reached-end--divider" />
      <NText>
        {{ t("page.explore.reached_end") }}
      </NText>
    </div>
  </div>
</template>

<style scoped>
.wall-container {
  display: flex;
  flex-direction: column;

  box-sizing: border-box;
  max-height: calc(
    100vh - v-bind(topAppBarHeight)
  ); /* 64px is the height of the top app bar */
  overflow-y: auto;
  padding: 16px 24px;

  user-select: none;
}

.reached-end {
  width: 30%;

  align-self: center;

  text-align: center;
}

.reached-end--divider {
  margin: 12px 0;
}
</style>
