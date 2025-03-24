<script setup lang="ts">
import { useRoute, useRouter } from "vue-router";
import NPost from "../../../components/NPost.vue";
import {
  computed,
  inject,
  type Ref,
  ref,
  toValue,
  useTemplateRef,
  watchEffect,
} from "vue";
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
import useComment from "../../../hooks/comment.ts";
import NCommentChain from "../../../components/NCommentChain.vue";
import NText from "../../../components/NText.vue";
import NCard from "../../../components/NCard.vue";
import type { UserProfile } from "../../../types/backend.ts";
import useUser from "../../../hooks/user.ts";
import NUserAvatar from "../../../components/NUserAvatar.vue";
import type { TextField } from "mdui/components/text-field";

const { getPost, deletePost } = usePost();
const { getUserProfile } = useUser();
const { getComment, getComments, createComment, deleteComment } = useComment();
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
const comments = computedAsync(async () => {
  try {
    return await getComments(id.value);
  } catch (e) {
    handleError(e as ErrorMessage, "comment");
    return [];
  }
});
const refreshComments = async () => {
  try {
    comments.value = await getComments(id.value);
  } catch (e) {
    handleError(e as ErrorMessage, "comment");
    comments.value = [];
  }
};

const contentEl = useTemplateRef<TextField>("contentEl");
const replyContext: {
  activeId: Ref<number | undefined>;
  context: {
    [replyTo: number]: typeof replyContext.activeContext;
  };
  activeContext: Ref<
    | {
        replyToUser: UserProfile | undefined;
        content: string;
        loading: boolean;
      }
    | undefined
  >;
} = {
  activeId: ref(undefined),
  context: {
    "-1": ref({
      replyToUser: undefined,
      content: "",
      loading: false,
    }),
  },
  activeContext: computed(() => {
    let commentId =
      replyContext.activeId.value === undefined
        ? -1
        : replyContext.activeId.value;

    return replyContext.context[commentId].value;
  }),
};

const { userInfo: currentUserInfo } = useSessionStore();

const l = inject(layout) as Layout;

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

const closeReplyPanel = () => {
  const replyPanel = document.querySelector(".reply-panel") as HTMLElement;
  replyPanel.classList.add("reply-panel_hidden");
};

const submitReply = async () => {
  if (replyContext.activeContext.value === undefined) {
    return;
  }

  if (contentEl.value?.reportValidity() !== true) {
    return;
  }

  replyContext.activeContext.value.loading = true;

  try {
    await createComment(id.value, {
      content: replyContext.activeContext.value.content,
      parentCommentID: replyContext.activeId.value,
    });
    closeReplyPanel();
    await refreshComments();
    snackbar({
      message: t("page.post.comment.submit.success"),
      closeable: true,
    });
  } catch (e) {
    handleError(e as ErrorMessage, "comment");
  } finally {
    replyContext.activeContext.value.loading = false;
  }
};

const performCommentAction = (name: string, commentId?: number) => {
  const actions = {
    reply: async (commentId?: number) => {
      if (commentId && !replyContext.context[commentId]) {
        replyContext.context[commentId] = ref({
          replyToUser: (await getUserProfile(
            (await getComment(id.value, commentId)).commenterID,
          ))!,
          content: "",
          loading: false,
        });
      }
      replyContext.activeId.value = commentId;
      const replyPanel = document.querySelector(".reply-panel") as HTMLElement;
      replyPanel.classList.remove("reply-panel_hidden");
    },
    delete: async (commentId: number) => {
      try {
        await confirm({
          headline: t("page.post.comment.delete.confirm.title"),
          description: t("page.post.comment.delete.confirm.description"),
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
        await deleteComment(id.value, commentId);
        await refreshComments();
        snackbar({
          message: t("page.post.comment.delete.success"),
          closeable: true,
        });
      } catch (e) {
        handleError(e as ErrorMessage, "comment");
      }
    },
  } as {
    [key: string]: (commentId?: number) => void;
  };

  if (actions[name]) {
    actions[name](commentId);
  }
};

watchEffect(() => {
  l.updateLayout({
    fab: {
      icon: "reply",
      onClick: () => performCommentAction("reply"),
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

const dividerMargin = computed(() => {
  if (l.isLargeScreen.value) {
    return "0 16px";
  } else {
    return "16px 0";
  }
});

const mainLayoutHeight = computed(
  () =>
    `calc(100vh - ${toValue(l.topAppBar)?.height ?? 0}px - ${toValue(l.bottomAppBar)?.height ?? 0}px)`,
);

const mainLayoutWidth = computed(
  () => `calc(100vw - ${toValue(l.navigationRail)?.width ?? 0}px)`,
);

const bottomAppBarHeight = computed(
  () => (toValue(l.bottomAppBar)?.height ?? 0) + "px",
);

useHead({
  title: computed(
    () => post.value?.title ?? post.value?.content.substring(0, 20) ?? "",
  ),
});
</script>

<template>
  <mdui-linear-progress v-if="post === undefined" />
  <div
    v-else
    class="outer-container"
    :class="{
      'large-screen': l.isLargeScreen.value,
    }"
  >
    <div class="article-container">
      <article class="post-panel">
        <n-post :the-post="post" />
      </article>
      <mdui-divider
        class="comments-divider"
        :vertical="l.isLargeScreen.value"
      />
      <div class="comments-panel">
        <template v-if="comments === undefined">
          <mdui-circular-progress />
        </template>
        <template v-else-if="comments.length === 0">
          <NCard>
            <NText>{{ t("page.post.comment.no_comment") }}</NText>
          </NCard>
        </template>
        <template v-else>
          <n-comment-chain
            v-for="root in comments"
            :key="root.comment.id"
            :the-comment-chain="root"
            @action="performCommentAction"
          />
        </template>
      </div>
    </div>
    <div class="reply-panel reply-panel_hidden">
      <div class="reply-panel_inner">
        <div class="reply-panel_header">
          <mdui-button-icon
            icon="close"
            class="reply-panel_header__close"
            @click="closeReplyPanel"
          />
          <div
            v-if="
              replyContext.activeId.value &&
              replyContext.activeContext.value !== undefined &&
              replyContext.activeContext.value.replyToUser !== undefined
            "
            class="reply-panel_header__reply-to-user"
          >
            <NUserAvatar :user="replyContext.activeContext.value.replyToUser" />
            <NText
              >{{
                t("page.post.comment.reply_to", {
                  username:
                    replyContext.activeContext.value.replyToUser.username ?? "",
                })
              }}
            </NText>
          </div>
        </div>
        <div class="reply-panel_main">
          <mdui-text-field
            v-if="replyContext.activeContext.value !== undefined"
            ref="contentEl"
            :value="replyContext.activeContext.value.content"
            @change.stop="
              replyContext.activeContext.value.content = $event.target.value
            "
            variant="outlined"
            autocomplete="off"
            enterkeyhint="done"
            autosize
            :label="t('content')"
            required
            maxlength="65535"
            counter
            :disabled="replyContext.activeContext.value.loading"
          />
        </div>
        <div class="reply-panel_footer">
          <mdui-button
            v-if="replyContext.activeContext.value !== undefined"
            :disabled="replyContext.activeContext.value.loading"
            @click="submitReply"
            icon="send"
            >{{ t("reply") }}
          </mdui-button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.outer-container {
  display: flex;

  padding: 16px 24px;

  box-sizing: border-box;
}

.large-screen.outer-container {
  height: v-bind(mainLayoutHeight);
  width: v-bind(mainLayoutWidth);
}

.article-container {
  display: flex;
  flex-direction: column;

  height: 100%;
  width: 100%;
}

.large-screen .article-container {
  display: grid;
  grid-template-columns: 2fr auto 1fr;
}

.large-screen .post-panel {
  height: 100%;
  overflow-y: auto;
}

.comments-panel {
  display: flex;
  flex-direction: column;

  gap: 16px;
}

.large-screen .comments-panel {
  height: 100%;
  overflow-y: auto;
}

.comments-panel > * {
  flex-shrink: 0;
}

.comments-divider {
  margin: v-bind(dividerMargin);
}

mdui-circular-progress {
  margin: auto;
}

.reply-panel {
  position: fixed;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);

  background-color: rgb(var(--mdui-color-primary-container));

  width: 100%;
  height: 30%;

  padding-bottom: calc(v-bind(bottomAppBarHeight));

  box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.1);
  border-radius: 16px 16px 0 0;

  transition: height 0.5s;
}

.large-screen .reply-panel {
  width: 80%;
  height: 50%;
}

.reply-panel.reply-panel_hidden {
  box-shadow: none;
  height: 0;
}

.reply-panel_inner {
  box-sizing: border-box;

  display: flex;
  flex-direction: column;

  height: 100%;

  gap: 16px;

  padding: 16px 24px;
}

.reply-panel_header {
  display: flex;
  flex-direction: row-reverse;
  justify-content: space-between;
}

.reply-panel_header__reply-to-user {
  display: flex;
  align-items: center;

  gap: 8px;
}

.reply-panel_main {
  flex: 1;
}

.reply-panel_footer {
  display: flex;
  justify-content: flex-end;
}
</style>
