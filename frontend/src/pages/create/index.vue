<script setup lang="ts">
import { computed, inject, ref, useTemplateRef } from "vue";
import { layout } from "../../utils/symbol.ts";
import type { Layout } from "../../types/layout.ts";
import { useI18n } from "vue-i18n";
import NPost from "../../components/NPost.vue";
import type { TextField } from "mdui/components/text-field.js";
import usePost from "../../hooks/post.ts";
import type { ErrorMessage } from "../../types/error-handling.ts";
import useErrorHandling from "../../hooks/error-handling.ts";
import { useRouter } from "vue-router";

const { t } = useI18n();
const { createPost } = usePost();
const { handle: handleError } = useErrorHandling();
const router = useRouter();

const currentTab = ref<"WRITE" | "PREVIEW">("WRITE");

const l = inject(layout) as Layout;

l.updateLayout({
  fab: {
    icon: "save",
    onClick: () => onSubmit(),
  },
});

const outContainerMargin = computed(() =>
  l.isLargeScreen.value ? "0 240px" : "0",
);

const titleEl = useTemplateRef<TextField>("titleEl");
const contentEl = useTemplateRef<TextField>("contentEl");
const { title, content } = {
  title: ref<string>(),
  content: ref<string>(""),
};
const loading = ref<boolean>(false);

const onSubmit = async () => {
  if (
    titleEl.value?.reportValidity() !== true ||
    contentEl.value?.reportValidity() !== true
  ) {
    return;
  }

  loading.value = true;

  try {
    const id = await createPost({
      title: title.value,
      content: content.value,
    });
    await router.replace({ name: "post-id", params: { id: id.toString() } });
  } catch (e) {
    handleError(e as ErrorMessage);
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <mdui-tabs :value="currentTab" @change="currentTab = $event.target.value">
    <mdui-tab value="WRITE">{{ t("page.create.write.title") }}</mdui-tab>
    <mdui-tab value="PREVIEW">{{ t("page.create.preview.title") }}</mdui-tab>

    <mdui-tab-panel slot="panel" value="WRITE" class="wall-container">
      <mdui-text-field
        ref="titleEl"
        :value="title"
        @change.stop="title = $event.target.value"
        variant="outlined"
        autocomplete="off"
        enterkeyhint="next"
        autosize
        :label="t('title')"
        maxlength="100"
        counter
        autofocus
        :disabled="loading"
      />
      <mdui-text-field
        ref="contentEl"
        :value="content"
        @change.stop="content = $event.target.value"
        variant="outlined"
        autocomplete="off"
        enterkeyhint="done"
        autosize
        min-rows="10"
        :label="t('content')"
        :helper="t('page.create.write.supporting')"
        required
        maxlength="65535"
        counter
        :disabled="loading"
      />
    </mdui-tab-panel>
    <mdui-tab-panel slot="panel" value="PREVIEW" class="wall-container">
      <div class="outer-container">
        <div class="container">
          <n-post
            :the-post="{
              id: -1,
              posterID: -1,
              title: title,
              content: content,
              createAt: new Date().toISOString(),
            }"
            :is-preview="true"
          />
        </div>
      </div>
    </mdui-tab-panel>
  </mdui-tabs>
</template>

<style scoped>
.wall-container {
  box-sizing: border-box;
  padding: 16px 24px;
}

.outer-container {
  display: flex;
  justify-content: center;
  align-items: center;
}

.container {
  box-sizing: border-box;

  margin: v-bind(outContainerMargin);
}
</style>
