<script setup lang="ts">
import type { Comment } from "../types/backend.ts";
import NCard from "./NCard.vue";
import NCardHeader from "./NCardHeader.vue";
import NCardMain from "./NCardMain.vue";
import { useTimeAgoLocalized } from "../utils/time.ts";
import NUserAvatar from "./NUserAvatar.vue";
import { computedAsync } from "@vueuse/core";
import useUser from "../hooks/user.ts";
import { useI18n } from "vue-i18n";
import NText from "./NText.vue";
import useComment from "../hooks/comment.ts";
import { useSessionStore } from "../stores/session.ts";

const { theComment } = defineProps<{
  theComment: Comment;
}>();

const emit = defineEmits<{
  action: [name: string, commentId: number];
}>();

const { t } = useI18n();

const { getUserProfile } = useUser();
const { getComment } = useComment();

const { userInfo: currentUserInfo } = useSessionStore();

const user = computedAsync(
  async () => await getUserProfile(theComment.commenterID),
);

const replyToUser = computedAsync(async () =>
  theComment.parentCommentID
    ? await getUserProfile(
        (await getComment(theComment.postID, theComment.parentCommentID))
          .commenterID,
      )
    : null,
);

const focusToComment = (commentId: number) => {
  const comment = document.querySelector(
    `[data-comment-id="${commentId}"]`,
  ) as HTMLElement | null;
  if (comment) {
    comment.scrollIntoView({ behavior: "smooth" });
    comment.classList.add("highlight-background-animation-running");
    comment.onanimationend = () => {
      comment.classList.remove("highlight-background-animation-running");
      comment.onanimationend = null;
    };
  }
};
</script>

<template>
  <NCard
    :data-comment-id="theComment.id"
    class="comment"
    :disabled="theComment.isDeleted"
  >
    <template #header>
      <NCardHeader
        :header="user?.username ?? theComment.commenterID.toString()"
        :subhead="useTimeAgoLocalized(theComment.createAt).value"
        class="card-header"
      >
        <template #monogram>
          <n-user-avatar :user="user" />
        </template>
        <template #button v-if="theComment.parentCommentID">
          <NText
            scale="label"
            color="secondary"
            @click="focusToComment(theComment.parentCommentID)"
            class="reply-to"
          >
            <mdui-icon name="reply" />
            <span>{{
              t("page.post.comment.reply_to", {
                username:
                  replyToUser?.username ??
                  theComment.parentCommentID.toString(),
              })
            }}</span>
          </NText>
        </template>
      </NCardHeader>
    </template>
    <NCardMain>
      <NText
        >{{
          theComment.isDeleted
            ? t("page.post.comment.tag.deleted.content")
            : theComment.content
        }}
      </NText>
      <template #actions v-if="!theComment.isDeleted">
        <mdui-chip
          icon="reply"
          variant="assist"
          @click="emit('action', 'reply', theComment.id)"
        >
          <NText>{{ t("reply") }}</NText>
        </mdui-chip>
        <mdui-chip
          v-if="theComment.commenterID === currentUserInfo?.id"
          icon="delete"
          variant="assist"
          @click="emit('action', 'delete', theComment.id)"
        >
          <NText>{{ t("delete") }}</NText>
        </mdui-chip>
      </template>
    </NCardMain>
  </NCard>
</template>

<style scoped>
.card-header {
  user-select: none;
}

.reply-to {
  cursor: pointer;

  display: flex;
  align-items: center;
  gap: 0.1em;

  & span:hover {
    text-decoration: underline;
  }

  padding-right: 0.5em;
}
</style>
