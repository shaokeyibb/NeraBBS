<script lang="ts" setup>
import {NuxtLink} from "#components";
import useLocale from "~/hooks/locale";
import {storeToRefs} from "pinia";
import {useUserInfoStore, useUserProfileStore} from "~/stores/user";
import useUser from "~/hooks/user";
import type {PreviewPost} from "~/data/common";
import usePost from "~/hooks/post";
import useUsers from "~/hooks/users";

const {$pinia} = useNuxtApp()

const sizeClass = ref<'compact' | 'medium' | 'expanded'>('expanded')
const panes = ref<1 | 2>(2)
const floatingActionButtonLabel = ref<string>('Label')

const {availableLocales, locale} = useI18n()
const {getLocalizedLocalName, setLocalePermanently} = useLocale()

const router = useRouter()

const isLanguageSelectorOpen = ref(false)
const selectedLanguage = ref<string>(locale.value)

const {signout} = useUser($pinia)
const {getUserProfile, getCachedUserProfile} = useUsers()
const {publishPost, getPreviewPosts} = usePost()

const {userInfo} = storeToRefs(useUserInfoStore())
const {userProfile} = storeToRefs(useUserProfileStore())

const pageSize = 20
const currentPage = ref(0)
const allPosts = reactive<PreviewPost[]>([])
const {data: newPagePosts, pending, error, refresh} = useAsyncData(() => getPreviewPosts(currentPage.value, pageSize), {
  lazy: true,
  server: false,
  watch: [currentPage],
})
watch(newPagePosts, (newVal) => {
  newVal && allPosts.push(...newVal)
})

const currentPostID = ref<number | undefined>(undefined)
watch(currentPostID, (value) => {
  if (!value || panes.value != 1) return
  router.push(`/posts/${value}`)
})
watch(panes, (value) => {
  if (value !== 1 || !currentPostID.value) return
  router.push(`/posts/${currentPostID.value}`)
})
</script>

<template>
  <HScaffold v-model:panes="panes"
             v-model:sizeClass="sizeClass"
             :align="'top'"
             :floatingActionButtonLabel="floatingActionButtonLabel"
             initial-panes="auto"
             initialSizeClass="auto">
    <template #header>
      <HTopAppBar :title="$t('app.name')" kind="small">
        <template v-if="sizeClass === 'compact'" #navigation-icon>
          <HIconButton :label="$t('menu')" :noTooltip="true" kind="standard">
            <svg class="icon-[material-symbols--menu]"/>
          </HIconButton>
        </template>
        <template #trailing>
          <HMenu>
            <template #button>
              <HIconButton :label="$t('user_profile')" :noTooltip="true" kind="standard">
                <img v-if="userProfile?.avatarPath"
                     :alt="$t('user_profile')"
                     :src="userProfile.avatarPath"
                     class="h-8 w-8 flex-shrink-0 mr-5"/>
                <svg v-else
                     class="icon-[material-symbols--account-circle]"/>
              </HIconButton>
            </template>
            <template #content>
              <div class="flex flex-row items-center mx-4 my-2">
                <img v-if="userProfile?.avatarPath" :alt="$t('user_profile')"
                     :src="userProfile.avatarPath"
                     class="h-8 w-8 flex-shrink-0 mr-5"/>
                <svg v-else class="h-8 w-8 flex-shrink-0 mr-5 icon-[material-symbols--account-circle]"/>
                <div v-if="userInfo && userProfile" class="flex flex-col">
                  <span v-if="userProfile.username" class="font-medium">{{ userProfile.username }}</span>
                  <span v-else class="font-medium">UID: {{ userInfo.id }}</span>
                  <span class="text-xs">{{ userInfo.email }}</span>
                </div>
                <span v-else class="font-medium">{{ $t('guest') }}</span>
              </div>
              <HMenuDivider/>
              <HMenuButton :as="NuxtLink"
                           :disabled="!userInfo"
                           :label="$t('settings')"
                           to="/settings">
                <template #leading>
                  <svg class="icon-[material-symbols--settings]"/>
                </template>
              </HMenuButton>
              <HMenuButton :label="$t('locale.name')"
                           :onClick="() => isLanguageSelectorOpen = true">
                <template #leading>
                  <svg class="icon-[material-symbols--language]"/>
                </template>
              </HMenuButton>
              <HMenuDivider/>
              <template v-if="userProfile">
                <HMenuButton :label="$t('signout')"
                             :onClick="() => signout()">
                  <template #leading>
                    <svg class="icon-[material-symbols--logout]"/>
                  </template>
                </HMenuButton>
              </template>
              <template v-else>
                <HMenuButton :as="NuxtLink"
                             :label="$t('signin')"
                             to="/signin">
                  <template #leading>
                    <svg class="icon-[material-symbols--login]"/>
                  </template>
                </HMenuButton>
                <HMenuButton :as="NuxtLink"
                             :label="$t('signup')"
                             to="/signup">
                  <template #leading>
                    <svg class="icon-[material-symbols--how-to-reg]"/>
                  </template>
                </HMenuButton>
              </template>
            </template>
          </HMenu>
        </template>
      </HTopAppBar>
    </template>
    <template #defaultContent>
      <div class="flex flex-col gap-3 pb-3">
        <HCard v-for="post in allPosts" :interactable="true" kind="filled" @click="currentPostID = post.id">
          <div class="flex flex-col py-3 gap-3">
            <div class="flex flex-row items-center">
              <div class="h-8 w-8 mr-2">
                <img v-if="getCachedUserProfile(post.posterID).data?.avatarPath"
                     :alt="$t('avatar')"
                     :src="getCachedUserProfile(post.posterID).data!.avatarPath"
                     class="h-full rounded-full"/>
                <svg v-else class="h-full w-full icon-[material-symbols--account-circle]"/>
              </div>
              <div class="flex flex-col">
                <span v-if="getCachedUserProfile(post.posterID).pending"
                      class="h-4 w-full bg-slate-200 animate-pulse rounded"/>
                <span v-else-if="getCachedUserProfile(post.posterID).error" class="text-sm">UID: {{
                    post.posterID
                  }}</span>
                <span v-else-if="getCachedUserProfile(post.posterID).data?.username" class="text-sm">{{
                    getCachedUserProfile(post.posterID).data!.username
                  }}</span>
                <time class="text-sm text-halcyon-on-surface-variant">{{
                    useTimeAgoLocalized(post.createAt).value
                  }}
                </time>
              </div>
            </div>
            <div>
              <h1 class="text-xl">{{ post.title }}</h1>
              <p class="text-base">{{ post.content }}</p>
            </div>
          </div>
        </HCard>
      </div>
    </template>
    <template #extraPaneContent>
      <Post v-if="currentPostID" :postID="currentPostID"
            :previewPost="allPosts.find(it=>it.id===currentPostID)"/>
    </template>
    <template #floatingActionButtonIcon>
      <svg class="icon-[material-symbols--add]"/>
    </template>
    <template #navigationRailContent>
    </template>
  </HScaffold>

  <HDialog v-model:open="isLanguageSelectorOpen"
           :actions="[{ label: $t('cancel'), onClick: () => {} }, { label: $t('save'), onClick: () => setLocalePermanently(selectedLanguage) }]"
           :description="$t('language_selector.subtitle')"
           :dividers="true"
           :title="$t('language_selector.title')">
    <template #icon>
      <svg class="icon-[material-symbols--language]"/>
    </template>
    <template #content>
      <ul class="mt-4">
        <li v-for="locale in availableLocales" class="py-3">
          <label class="flex flex-row items-center">
            <input :checked="selectedLanguage === locale" class="w-5 h-5" name="lang" type="radio"
                   @change="() => selectedLanguage = locale"/>
            <span class="pl-3">{{ getLocalizedLocalName(locale) }}</span>
          </label>
        </li>
      </ul>
    </template>
  </HDialog>
</template>

<style scoped>
.menu-items {
  max-width: 100vw;
}

.card + .card {
  margin: 0;
}

.card h1.text-xl {
  font-size: 1.25rem /* 20px */;
  line-height: 1.75rem /* 28px */;
}

.card p.text-base {
  font-size: 0.875rem /* 14px */;
  line-height: 1.25rem /* 20px */;
}

header {
  background-color: var(--halcyon-surface);
}
</style>