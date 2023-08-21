<script lang="ts" setup>

import usePasskey from "~/hooks/passkey";

const {isSupported: isSupportedPasskey, createPasskeyCredential} = usePasskey();

</script>

<template>
  <div class="flex-auto container flex flex-col mx-auto my-4 gap-4 px-3">
    <div class="flex flex-col lg:flex-row justify-between items-center">
      <h1 class="text-3xl dark:text-white">{{ $t("settings.security") }}</h1>
      <h2 class="text-lg dark:text-white">{{ $t("settings.security.subtitle") }}</h2>
    </div>
    <div class="flex flex-col gap-4">
      <div class="flex flex-row justify-between items-center gap-2">
        <div class="flex flex-col gap-2">
          <h3 class="dark:text-white">{{ $t("settings.security.passkey") }}</h3>
          <span class="text-sm text-neutral-500">{{ $t("settings.security.passkey.subtitle") }}</span>
        </div>
        <button v-if="isSupportedPasskey"
                class="border rounded py-3 px-5 dark:text-white bg-secondary dark:bg-black"
                type="button"
                @click="createPasskeyCredential">
          {{ $t("settings.security.passkey.setup") }}
        </button>
        <span v-else class="text-sm text-red-600">{{ $t("settings.security.passkey.unsupported") }}</span>
      </div>
    </div>
  </div>
</template>