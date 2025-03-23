<script setup lang="ts">
import { useRoute, useRouter } from "vue-router";
import NPost from "../../../components/NPost.vue";
import { computed, inject, ref, watchEffect } from "vue";
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
      <div class="container">
        <n-post :the-post="post" />
        <div class="comments-panel"></div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.outer-container {
  display: flex;
  justify-content: center;
  align-items: center;

  padding: 16px 24px;
}

.container {
  box-sizing: border-box;

  margin: v-bind(outContainerMargin);
}
</style>
