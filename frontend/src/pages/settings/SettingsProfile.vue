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
