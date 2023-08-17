<script lang="ts" setup>

import MarkdownEditor from "~/components/MarkdownEditor.vue";
import usePublish from "~/hooks/publish";
import {withJumpUnauthorize} from "~/utils/fetch";
import {useUserInfoStore} from "~/stores/user_info";
import {storeToRefs} from "pinia";

const content = ref<string | undefined>()
const title = ref<string | undefined>()

const {t} = useI18n()

const {publishPost} = usePublish()

const {userInfo} = storeToRefs(useUserInfoStore())

async function onSubmitPublishPost() {
  if (!content.value || /^\s*$/.test(content.value)) {
    alert(t('publish.editor.validation.empty'))
    return
  }

  try {
    await withJumpUnauthorize(publishPost({
      title: title.value,
      content: content.value
    }), "/signin")
  } catch (e: any) {
    if (e.status === 400) {
      alert(t("publish.error.bad_request"))
      return
    }
    alert(t("publish.error.internal", {err: e.data.message}))
  }
}
</script>

<template>
  <div class="flex-auto container mx-auto my-3 px-4 py-6">
  </div>
  <ClientOnly>
    <div v-if="userInfo"
         class="container mx-auto border dark:border-0 shadow my-3">
      <MarkdownEditor v-model:model-value="content"
                      v-model:title="title"
                      :placeholder="$t('publish.editor.placeholder')"
                      :show-title="true"
                      :title-placeholder="$t('publish.editor.title_placeholder')">
        <template #actions>
          <button class="p-2 lg:px-4 lg:py-2 bg-secondary dark:bg-black dark:text-white"
                  @click="onSubmitPublishPost">
            <span aria-hidden="true"
                  class="icon-[material-symbols--send] dark:text-white text-2xl lg:mr-1 align-top"/>
            <span class="dark:text-white hidden lg:inline">{{ $t('publish.editor.submit') }}</span>
          </button>
        </template>
      </MarkdownEditor>
    </div>
    <div v-else
         class="flex flex-row w-fit mx-auto border border-black dark:border-white my-3 px-5 py-3">
      <span aria-hidden="true"
            class="icon-[material-symbols--error] dark:text-white text-2xl lg:mr-1 align-top"/>
      <span class="dark:text-white">{{ $t("publish.editor.required_sign_in") }}</span>
    </div>
    <template #fallback>
      <div class="flex flex-row w-fit mx-auto border border-black dark:border-white my-3 px-5 py-3">
        <span class="dark:text-white">Enable JavaScript to show the editor.</span>
      </div>
    </template>
  </ClientOnly>
</template>