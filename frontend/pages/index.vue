<script lang="ts" setup>

import MarkdownEditor from "~/components/MarkdownEditor.vue";
import usePost from "~/hooks/post";
import {withJumpUnauthorize} from "~/utils/fetch";
import {useUserInfoStore} from "~/stores/user_info";
import {storeToRefs} from "pinia";
import {PreviewPost} from "~/data/common";
import {useTimeAgoLocalized} from "~/utils/time";
import useUsers from "~/hooks/users";

const content = ref<string | undefined>()
const title = ref<string | undefined>()

const {t} = useI18n()

const {publishPost, getPreviewPosts} = usePost()
const {getUserInfo} = useUsers()
const router = useRouter()

const {userInfo} = storeToRefs(useUserInfoStore())

async function onSubmitPublishPost() {
  if (!content.value || /^\s*$/.test(content.value)) {
    alert(t('publish.editor.validation.empty'))
    return
  }

  try {
    const id = await withJumpUnauthorize(publishPost({
      title: title.value,
      content: content.value
    }), "/signin")

    await router.push(`/posts/${id}`)
  } catch (e: any) {
    if (e.status === 400) {
      alert(t("publish.error.bad_request"))
      return
    }
    alert(t("publish.error.internal", {err: e.data.message}))
  }
}

const currentPage = ref(0)
const allPosts = reactive<PreviewPost[]>([])
const {data, pending, error, refresh} = useAsyncData(() => getPreviewPosts(currentPage.value), {
  lazy: true,
  server: false,
  watch: [currentPage],
})
watch(data, (newVal) => {
  newVal && allPosts.push(...newVal)
})

const userNames = reactive<Record<number, {
  pending: boolean,
  error?: Error,
  data?: string
}>>({})

function getUserName(id: number) {
  if (!userNames[id]) {
    requireUserName(id)
  }
  return userNames[id]
}

async function requireUserName(id: number) {
  const data = getUserInfo(id)
  userNames[id] = {
    pending: true,
  }
  try {
    userNames[id].data = (await data).username
  } catch (e: any) {
    userNames[id].error = e
  } finally {
    userNames[id].pending = false
  }
  return data
}
</script>

<template>
  <div class="flex-auto container flex flex-col mx-auto my-4 gap-4">
    <div v-for="post in allPosts" :key="post.id"
         class="flex flex-col border shadow px-5 py-4"
         @click="$router.push(`/posts/${post.id}`)">
      <div class="flex flex-row items-center gap-2">
        <span v-if="getUserName(post.posterID).pending"
              class="h-4 w-20 bg-slate-200 animate-pulse"/>
        <span v-else-if="getUserName(post.posterID).error"
              class="font-bold dark:text-white">UID: {{ post.posterID }}</span>
        <span v-else class="font-bold dark:text-white">@{{ getUserName(post.posterID).data }}</span>
        <span class="text-neutral-600 text-sm dark:text-white">{{
            useTimeAgoLocalized(post.createAt).value
          }}</span>
      </div>
      <span v-if="post.title"
            class="text-lg dark:text-white">{{ post.title }}</span>
      <span class="dark:text-white">{{ post.content }}</span>
    </div>
    <div class="mx-auto my-5">
      <button v-if="pending" class="border rounded py-3 px-5 dark:text-white bg-secondary dark:bg-black"
              disabled>
        <span
            class="icon-[material-symbols--progress-activity] dark:text-white text-2xl lg:mr-1 align-top animate-spin"/>
        <span class="dark:text-white">{{ $t("index.loading") }}</span>
      </button>
      <button v-else-if="error" class="border rounded py-3 px-5 dark:text-white bg-secondary dark:bg-black"
              @click="refresh">
        <span class="icon-[material-symbols--sync-problem] text-red-600 text-2xl lg:mr-1 align-top"/>
        <span class="dark:text-white">{{ $t("index.error_retry") }}</span>
      </button>
      <button v-else :disabled="!data || data.length == 0"
              class="border rounded py-3 px-5 dark:text-white bg-secondary dark:bg-black"
              @click="currentPage++">
        <span class="icon-[material-symbols--more-horiz] dark:text-white text-2xl lg:mr-1 align-top"/>
        <span class="dark:text-white">{{ $t("index.load_more") }}</span>
      </button>
    </div>
  </div>
  <div class="mx-auto py-10"/>
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