<script setup lang="ts">
import NCardMain from "../../components/NCardMain.vue";
import {useI18n} from "vue-i18n";
import NText from "../../components/NText.vue";
import usePasskey from "../../hooks/passkey.ts";
import {computedAsync, useMemoize} from "@vueuse/core";
import useErrorHandling from "../../hooks/error-handling.ts";
import type {ErrorMessage} from "../../types/error-handling.ts";
import {isSupported as _isSupportedPasskey} from "../../utils/passkey.ts";
import {useTimeAgoLocalized} from "../../utils/time.ts";
import {prompt} from "mdui/functions/prompt";
import useUser from "../../hooks/user.ts";
import {snackbar} from "mdui";

const { insideCard = false } = defineProps<{
  insideCard?: boolean;
}>();

const { t } = useI18n();

const { handle: handleError } = useErrorHandling();
const {
  getPasskeys,
  createPasskeyCredential,
  removePasskey: _removePasskey,
} = usePasskey();
const { changePassword: _changePassword } = useUser();

const getMemorizedPasskey = useMemoize(getPasskeys);
const passkeys = computedAsync(async () => {
  try {
    return await getMemorizedPasskey();
  } catch (e) {
    handleError(e as ErrorMessage, "passkey");
  }
});

const createPasskey = async () => {
  try {
    await createPasskeyCredential();
  } catch (e) {
    handleError(e as ErrorMessage, "passkey");
  }

  await getMemorizedPasskey.load();
};

const removePasskey = async (id: number) => {
  try {
    await _removePasskey(id);
  } catch (e) {
    handleError(e as ErrorMessage, "passkey");
  }

  await getMemorizedPasskey.load();
};

const isSupportedPasskey = computedAsync(
  async () => await _isSupportedPasskey(),
);

const changePassword = async () => {
  let res: string;
  try {
    res = await prompt({
      headline: t("page.settings.security.change_password"),
      icon: "password",
      description: t("page.settings.security.change_password.subtitle"),
      confirmText: t("confirm"),
      cancelText: t("cancel"),
      queue: "change-password",
      textFieldOptions: {
        errorIcon: "error",
        label: t("new_password"),
        autocomplete: "new-password",
        minlength: 8,
        required: true,
        togglePassword: true,
        type: "password",
        variant: "outlined",
      },
    });
  } catch {
    // ignore cancel error
    return;
  }
  await _changePassword(res);
  snackbar({
    message: t("page.settings.security.change_password.success"),
    closeable: true,
  });
};
</script>

<template>
  <component :is="insideCard ? NCardMain : 'div'" class="section">
    <div class="list">
      <div class="list-headline">
        <NText scale="label" size="large"
          >{{ t("page.settings.security.change_password") }}
        </NText>
        <NText scale="body" size="small"
          >{{ t("page.settings.security.change_password.subtitle") }}
        </NText>
      </div>
      <mdui-button class="button" @click="changePassword()">
        {{ t("page.settings.security.change_password.perform") }}
      </mdui-button>
    </div>
    <div class="list">
      <div class="list-headline">
        <NText scale="label" size="large"
          >{{ t("page.settings.security.passkey") }}
        </NText>
        <NText scale="body" size="small"
          >{{ t("page.settings.security.passkey.subtitle") }}
        </NText>
      </div>
      <mdui-list>
        <mdui-list-item
          v-for="item in passkeys"
          :key="item.id"
          :headline="item.nickname"
          :description="
            t('page.settings.security.passkey.single.description', {
              createAt: useTimeAgoLocalized(item.createAt).value,
            })
          "
          nonclickable
        >
          <mdui-button-icon
            slot="end-icon"
            icon="delete"
            @click="removePasskey(item.id)"
          />
        </mdui-list-item>
      </mdui-list>
      <mdui-button
        class="button"
        @click="createPasskey()"
        :disabled="!isSupportedPasskey"
      >
        {{ t("page.settings.security.passkey.add_one") }}
      </mdui-button>
      <NText v-if="!isSupportedPasskey" as="div" color="error">
        {{ t("page.settings.security.passkey.unsupported") }}
      </NText>
    </div>
  </component>
</template>

<style scoped>
.section:deep(> div) {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.list {
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.list-headline {
  display: flex;
  flex-direction: column;
  gap: 1px;
}

.button {
  margin-top: 8px;
}
</style>
