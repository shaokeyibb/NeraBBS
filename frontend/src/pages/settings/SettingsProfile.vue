<script setup lang="ts">
import NCardMain from "../../components/NCardMain.vue";
import { useSessionStore } from "../../stores/session.ts";
import { storeToRefs } from "pinia";
import { useI18n } from "vue-i18n";
import { ref } from "vue";
import { useFileDialog } from "@vueuse/core";
import useUser from "../../hooks/user.ts";
import useErrorHandling from "../../hooks/error-handling.ts";
import type { ErrorMessage } from "../../types/error-handling.ts";
import { prompt } from "mdui/functions/prompt.js";

const { insideCard = false } = defineProps<{
  insideCard?: boolean;
}>();

const { handle: handleError } = useErrorHandling();

const { t } = useI18n();

const { patchUserProfile } = useUser();

const { userProfile } = storeToRefs(useSessionStore());

const { open: openAvatarFileDialog, onChange: onAvatarFileDialogChange } =
  useFileDialog({
    accept: "image/*",
    multiple: false,
    reset: true,
  });

const loading = ref<boolean | "AVATAR_UPLOADING">(false);

const onUploadAvatar = async (files?: FileList | null) => {
  if (files == undefined) {
    openAvatarFileDialog();
    return;
  }

  loading.value = "AVATAR_UPLOADING";

  const [file] = files;

  try {
    userProfile.value = await patchUserProfile({
      avatar: file,
    });
  } catch (e) {
    handleError(e as ErrorMessage);
  } finally {
    loading.value = false;
  }
};

onAvatarFileDialogChange(onUploadAvatar);

const onModifyTextProfile = async (e: MouseEvent) => {
  const field = (e.target as HTMLElement).dataset.field!;

  let res: string;
  try {
    res = await prompt({
      headline: t("page.settings.profile.modify_text.headline", {
        field: t(field),
      }),
      icon: "edit",
      description: t("page.settings.profile.modify_text.description"),
      confirmText: t("confirm"),
      cancelText: t("cancel"),
      closeOnEsc: true,
      closeOnOverlayClick: true,
      queue: "modify-text-profile",
    });
  } catch {
    // ignore cancel error
    return;
  }

  loading.value = true;

  try {
    userProfile.value = await patchUserProfile({
      [field]: res,
    });
  } catch (e) {
    handleError(e as ErrorMessage);
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <component :is="insideCard ? NCardMain : 'div'" class="list">
    <mdui-list>
      <mdui-list-item @click="onUploadAvatar()" :disabled="loading !== false">
        {{ t("avatar") }}
        <mdui-circular-progress
          v-if="loading === 'AVATAR_UPLOADING'"
          slot="end-icon"
        />
        <mdui-avatar v-else slot="end-icon" :src="userProfile?.avatarPath" />
      </mdui-list-item>
      <mdui-list-item
        :headline="t('username')"
        :description="userProfile?.username"
        end-icon="arrow_right"
        data-field="username"
        @click="onModifyTextProfile"
        :disabled="loading !== false"
      />
      <mdui-list-item
        :headline="t('signature')"
        :description="userProfile?.signature"
        end-icon="arrow_right"
        data-field="signature"
        @click="onModifyTextProfile"
        :disabled="loading !== false"
      />
    </mdui-list>
  </component>
</template>

<style scoped>
.list {
  display: flex;
  flex-direction: column;
  gap: 3px;
}
</style>
