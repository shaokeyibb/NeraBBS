<script lang="ts" setup>
import usePasskey from "~/hooks/passkey";

const {isSupported: _isSupportedPasskey, createPasskeyCredential} = usePasskey();

const {state: isSupportedPasskey} = useAsyncState(_isSupportedPasskey, false)
</script>

<template>
  <div class="mx-5">
    <div class="flex flex-col">
      <div class="flex flex-row justify-between items-center py-4">
        <div class="flex flex-col">
          <h3>{{ $t("settings.security.passkey") }}</h3>
          <span class="text-sm mt-1">{{ $t("settings.security.passkey.subtitle") }}</span>
        </div>
        <div class="ml-1">
          <HButton v-if="isSupportedPasskey" :content="$t('setup')" kind="filled" @click="createPasskeyCredential"/>
          <HTooltip v-else :content="$t('settings.security.passkey.unsupported')">
            <HButton :content="$t('setup')" disabled kind="filled"/>
          </HTooltip>
        </div>
      </div>
    </div>
  </div>
</template>