<script lang="ts" setup>
import BackableComponent from "~/components/utils/BackableComponent.vue";
import usePost from "~/hooks/post";
import useUsers from "~/hooks/users";
import type {Post, PreviewPost} from "~/data/common";
import MarkdownViewer from "~/components/MarkdownViewer.vue";

const props = defineProps<{
  postID: number,
  previewPost?: PreviewPost,
}>()

const {t} = useI18n()
const {getPost} = usePost()
const {getCachedUserProfile} = useUsers()

const {data: _data, pending, error} = useAsyncData<Post>(props.postID.toString(), () => getPost(props.postID), {
  lazy: true,
})

const data = computed<Post | null>(() => {
  if (pending.value && props.previewPost) {
    return props.previewPost
  }
  return _data.value
})

</script>

<template>
  <BackableComponent :title="data?.title ?? $t('posts.title')" back-to="/">
    <template v-if="error">
      <span>{{ t("posts.failed.internal", {err: error.message}) }}</span>
    </template>
    <template v-else-if="data">
      <HCard kind="filled">
        <div class="flex flex-row items-center">
          <div class="h-16 w-16 mr-2">
            <img v-if="getCachedUserProfile(data.posterID).data?.avatarPath"
                 :alt="$t('avatar')"
                 :src="getCachedUserProfile(data.posterID).data!.avatarPath"
                 class="h-full rounded-full"/>
            <svg v-else class="h-full w-full icon-[material-symbols--account-circle]"/>
          </div>
          <div class="flex flex-col">
                <span v-if="getCachedUserProfile(data.posterID).pending"
                      class="h-4 w-full bg-slate-200 animate-pulse rounded"/>
            <span v-else-if="getCachedUserProfile(data.posterID).error" class="text-sm">UID: {{
                data.posterID
              }}</span>
            <span v-else-if="getCachedUserProfile(data.posterID).data?.username" class="text-sm">{{
                getCachedUserProfile(data.posterID).data!.username
              }}</span>
            <time class="text-sm text-halcyon-on-surface-variant">{{
                useTimeAgoLocalized(data.createAt).value
              }}
            </time>
          </div>
        </div>
      </HCard>
      <MarkdownViewer :value="data.content" class="my-3"/>
      <div v-if="pending" class="space-y-3 animate-pulse">
        <div v-for="_ in 10" class="h-4 bg-slate-200 rounded"/>
      </div>
    </template>
  </BackableComponent>
</template>