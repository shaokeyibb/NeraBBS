<script lang="ts" setup>
import type {UserProfilePatchReq} from "~/data/common";
import {ComputedRef} from "vue";
import {storeToRefs} from "pinia";
import useUser from "~/hooks/user";
import {useUserInfoStore, useUserProfileStore} from "~/stores/user";
import {snackbar} from "halcyon-vue";

const {
  files: avatarFiles,
  open: openAvatarDialog,
  onChange: onAvatarChange
} = useFileDialog({
  accept: "image/*",
  multiple: false,
  reset: true,
})
const {$pinia} = useNuxtApp()
const {t} = useI18n()
const {updateUserProfile} = useUser($pinia)

const isUserProfileChanged = ref(false)
const isUserProfileUpdating = ref(false)
const patchedUserProfile = reactive<UserProfilePatchReq>({})

const isUploadAvatarConfirmDialogOpen = ref(false)
const uploadingAvatarUrl = computed(() => avatarFiles.value && avatarFiles.value[0] ? URL.createObjectURL(avatarFiles.value[0]) : undefined)

onAvatarChange((avatar) => {
  if (!avatar?.length) return
  isUploadAvatarConfirmDialogOpen.value = true
})

function onConfirmAvatarChange() {
  userProfile.value!.avatarPath = uploadingAvatarUrl.value
  patchedUserProfile.avatar = avatarFiles.value![0]
  isUserProfileChanged.value = true
}

const {userProfile} = storeToRefs(useUserProfileStore())
const {userInfo} = storeToRefs(useUserInfoStore())
//

type FormItem = {
  title: string
  subtitle?: string
  mutable: boolean
  shrinkable?: boolean
  value?: ComputedRef
  onChange?: (value: string) => void
}

const form = shallowRef<FormItem[]>([
  {
    title: t("settings.profile.form.user_id.title"),
    mutable: false,
    value: computed(() => '' + userProfile.value!.userID),
  },
  {
    title: t("settings.profile.form.username.title"),
    mutable: true,
    value: computed(() => userProfile.value!.username),
    onChange: (value) => {
      userProfile.value!.username = value
      patchedUserProfile.username = value
      isUserProfileChanged.value = true
    }
  },
  {
    title: t("settings.profile.form.signature.title"),
    mutable: true,
    shrinkable: false,
    value: computed(() => userProfile.value!.signature),
    onChange: (value) => {
      userProfile.value!.signature = value
      patchedUserProfile.signature = value
      isUserProfileChanged.value = true
    }
  },
  {
    title: t("settings.profile.form.email.title"),
    subtitle: t("settings.profile.form.email.subtitle"),
    mutable: false,
    value: computed(() => userInfo.value!.email),
  },
  {
    title: t("settings.profile.form.create_at.title"),
    subtitle: t("settings.profile.form.create_at.subtitle", {"app_name": t("app.name")}),
    mutable: false,
    value: computed(() => new Date(userInfo.value!.createAt).toString())
  },
])

async function onSaveProfile() {
  isUserProfileUpdating.value = true
  try {
    await updateUserProfile(patchedUserProfile)
    snackbar.send({
      message: t("settings.profile.save.succeed"),
    })
  } catch (e: unknown) {
    snackbar.send({
      message: t("settings.profile.save.failed", {reason: e}),
    })
  } finally {
    isUserProfileUpdating.value = false
  }
}
</script>

<template>
  <div class="mx-5">
    <div class="flex flex-col items-center">
      <div class="w-40 h-40 py-4" @click="openAvatarDialog()">
        <img v-if="userProfile && userProfile.avatarPath" :alt="$t('avatar')" :src="userProfile.avatarPath"
             class="h-full rounded-full"/>
        <svg v-else class="h-full w-full icon-[material-symbols--account-circle]"/>
      </div>
    </div>
    <div class="grid lg:grid-cols-2 grid-cols-1 gap-4">
      <div v-for="item in form" :class="[
          item.shrinkable != false ? 'lg:col-span-1' : 'lg:col-span-2',
      ]">
        <HTextField :disabled="!item.mutable"
                    :label="item.title"
                    :model-value="item.value?.value"
                    :name="item.title"
                    :placeholder="$t('settings.profile.form.empty.placeholder')"
                    kind="outlined"
                    @update:model-value="item.onChange">
          <template #helper><span>{{ item.subtitle }}</span></template>
        </HTextField>
      </div>
    </div>
  </div>

  <div v-if="isUserProfileChanged && !isUserProfileUpdating" class="fixed bottom-0 right-0">
    <HFloatingActionButton :content="$t('save')" :label="$t('save')" kind="extended" @click="onSaveProfile">
      <svg class="icon-[material-symbols--save]"/>
    </HFloatingActionButton>
  </div>

  <HDialog v-model:open="isUploadAvatarConfirmDialogOpen"
           :actions="[{ label: $t('cancel'), onClick: () => {} }, { label: $t('confirm'), onClick: () => onConfirmAvatarChange() }]"
           :description="$t('avatar_confirmer.subtitle')"
           :title="$t('avatar_confirmer.title')">
    <template #icon>
      <svg class="icon-[material-symbols--account-circle]"/>
    </template>
    <template #content>
      <div class="mt-4">
        <img :alt="$t('avatar')" :src="uploadingAvatarUrl"/>
      </div>
    </template>
  </HDialog>

  <HSnackbar align="center"/>
</template>