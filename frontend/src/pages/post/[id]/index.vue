<script setup lang="ts">
import { useRoute, useRouter } from "vue-router";
import NPost from "../../../components/NPost.vue";
import { computed, inject, ref, toValue, watchEffect } from "vue";
import usePost from "../../../hooks/post.ts";
import { computedAsync } from "@vueuse/core";
import { layout } from "../../../utils/symbol.ts";
import type { Layout } from "../../../types/layout.ts";
import { useHead } from "@unhead/vue";
import { useI18n } from "vue-i18n";
import { useSessionStore } from "../../../stores/session.ts";
import useErrorHandling from "../../../hooks/error-handling.ts";
import type { ErrorMessage } from "../../../types/error-handling.ts";

import { confirm } from "mdui/functions/confirm.js";
import { snackbar } from "mdui/functions/snackbar.js";

const { getPost, deletePost } = usePost();
const router = useRouter();
const route = useRoute();
const { t } = useI18n();
const { handle: handleError } = useErrorHandling();

const error = ref(false);

const id = computed(() => parseInt(route.params.id as string));
const post = computedAsync(async () => {
  try {
    return await getPost(id.value);
  } catch (e) {
    error.value = true;
    handleError(e as ErrorMessage, "post");
    return undefined;
  }
});

const { userInfo: currentUserInfo } = useSessionStore();

const l = inject(layout) as Layout;

const topAppBarHeight = computed(
  () => (toValue(l?.topAppBar)?.height ?? 0) + "px",
);

const bottomAppBarHeight = computed(
  () => (toValue(l?.bottomAppBar)?.height ?? 0) + "px",
);

const outContainerMargin = computed(() =>
  l.isLargeScreen.value ? "0 240px" : "0",
);

const actions = [
  {
    icon: "delete",
    tooltip: computed(() => t("delete")),
    onClick: async () => {
      try {
        await confirm({
          headline: t("page.post.delete.confirm.title"),
          description: t("page.post.delete.confirm.description"),
          confirmText: t("confirm"),
          cancelText: t("cancel"),
          closeOnEsc: true,
          closeOnOverlayClick: true,
        });
      } catch {
        // ignore cancel error
        return;
      }

      try {
        await deletePost(id.value);
        await router.push({ name: "index" });
        snackbar({
          message: t("page.post.delete.success"),
          closeable: true,
        });
      } catch (e) {
        handleError(e as ErrorMessage, "post");
      }
    },
    privilege: true,
  },
];

watchEffect(() => {
  l.updateLayout({
    fab: {
      icon: "reply",
      onClick: () => {},
    },
    topAppBar: {
      leadingIconBtn: {
        icon: "arrow_back",
        onClick: () => router.back(),
      },
    },
    bottomAppBar: {
      actions: actions.filter(
        (action) =>
          !action.privilege ||
          (currentUserInfo && currentUserInfo.id === post.value?.posterID),
      ),
    },
  });
});

useHead({
  title: computed(() => post.value?.title ?? ""),
});
</script>

<template>
  <div v-if="!error">
    <mdui-linear-progress v-if="post === undefined" />
    <div v-else class="outer-container">
      <div class="wall-container">
        <n-post :the-post="post" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.outer-container {
  position: relative;
  overflow: hidden;
  min-height: calc(
    100vh - v-bind(topAppBarHeight)
  ); /* 64px is the height of the top app bar */

  margin: v-bind(outContainerMargin);
}

.wall-container {
  display: flex;
  flex-direction: column;

  position: relative;

  box-sizing: border-box;
  max-height: calc(
    100vh - v-bind(topAppBarHeight) - v-bind(bottomAppBarHeight)
  ); /* 64px is the height of the top app bar */
  overflow: auto;
  padding: 16px 24px;
}
</style>
