<script lang="ts" setup>
import MarkdownViewer from "~/components/MarkdownViewer.vue";
import usePost from "~/hooks/post";
import useUsers from "~/hooks/users";
import {Post, UserInfo} from "~/data/common";

const route = useRoute()
const {getPost} = usePost()
const {getUserInfo} = useUsers()

const id = route.params.id as string

const {data, pending, error} = useAsyncData<Post>(() => getPost(id), {
  lazy: true,
})

const {
  data: userData,
  pending: userPending,
  error: userError
} = useAsyncData<UserInfo>(() => data.value ? getUserInfo(data.value.posterID) : Promise.reject(), {
  lazy: true,
  pick: ['username'],
  watch: [data]
})

</script>

<template>
  <div v-if="pending"
       class="max-w-screen-lg flex flex-col mx-auto gap-2">
    <span class="h-7 w-full bg-slate-200 animate-pulse mb-4"/>
    <span v-for="_ in 10" class="h-5 w-full bg-slate-200 animate-pulse"/>
  </div>
  <div v-else-if="error"
       class="flex flex-row w-fit mx-auto border border-black dark:border-white my-3 px-5 py-3">
    <span aria-hidden="true"
          class="icon-[material-symbols--error] dark:text-white text-2xl lg:mr-1 align-top"/>
    <span class="dark:text-white">{{ $t("posts.error") }}</span>
  </div>
  <template v-else-if="!data"/>
  <article v-else
           class="max-w-screen-lg flex flex-col mx-auto my-4 px-10 lg:px-0">
    <div class="pb-4 border-b">
      <h1 v-if="data.title"
          class="text-3xl dark:text-white">{{ data.title }}</h1>
      <div class="flex flex-row items-center gap-2">
        <span v-if="userPending"
              class="h-4 w-20 bg-slate-200 animate-pulse"/>
        <span v-else-if="userError"
              class="font-bold dark:text-white">UID: {{ data.posterID }}</span>
        <span v-else
              class="font-bold dark:text-white">@{{ userData?.username }}</span>
        <time :datetime="data.createAt"
              class="text-neutral-600 text-sm dark:text-white">
          {{ useTimeAgoLocalized(data.createAt).value }}
        </time>
      </div>
    </div>
    <article class="prose dark:prose-invert mx-auto">
      <MarkdownViewer :value="data.content"/>
    </article>
  </article>
</template>