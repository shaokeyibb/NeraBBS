<script setup lang="ts">
import NCardMain from "../../components/NCardMain.vue";
import { useI18n } from "vue-i18n";
import NText from "../../components/NText.vue";
import usePasskey from "../../hooks/passkey.ts";
import { computedAsync, useMemoize } from "@vueuse/core";
import useErrorHandling from "../../hooks/error-handling.ts";
import type { ErrorMessage } from "../../types/error-handling.ts";
import { isSupported as _isSupportedPasskey } from "../../utils/passkey.ts";
import { useTimeAgoLocalized } from "../../utils/time.ts";

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
</script>

<template>
  <component :is="insideCard ? NCardMain : 'div'" class="list">
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
      <mdui-button
        class="add-one-button"
        @click="createPasskey()"
        :disabled="!isSupportedPasskey"
      >
        {{ t("page.settings.security.passkey.add_one") }}
      </mdui-button>
      <NText v-if="!isSupportedPasskey" as="div" color="error">
        {{ t("page.settings.security.passkey.unsupported") }}
      </NText>
    </mdui-list>
  </component>
</template>

<style scoped>
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

.add-one-button {
  margin-top: 8px;
}
</style>
