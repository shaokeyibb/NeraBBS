<script lang="ts" setup>
import NText from "../../components/NText.vue";
import type { TextField } from "mdui/components/text-field.js";
import type { Button } from "mdui/components/button.js";

import { useI18n } from "vue-i18n";
import useErrorHandling from "../../hooks/error-handling.ts";
import type { ErrorMessage } from "../../types/error-handling.ts";
import useAuth from "../../hooks/auth.ts";
import { useRouter } from "vue-router";
import { onMounted, onUnmounted, ref, useTemplateRef } from "vue";
import { isSupported as _isSupportedPasskey } from "../../utils/passkey.ts";
import { computedAsync } from "@vueuse/core";
import usePasskey from "../../hooks/passkey.ts";

const { t } = useI18n();
const { handle: handleError } = useErrorHandling();
const { signIn, signUp } = useAuth();
const { validatePasskeyCredential } = usePasskey();
const router = useRouter();

const emailEl = useTemplateRef<TextField>("emailEl");
const passwordEl = useTemplateRef<TextField>("passwordEl");
const passkeyEl = useTemplateRef<Button>("passkeyEl");

const loading = ref(false);
const isPasskeyConditionalUIRunning = ref(false);
const isSupportedPasskey = computedAsync(_isSupportedPasskey, false);

const onSubmit = async (e: MouseEvent) => {
  const button = e.target as Button;

  if (
    emailEl.value?.reportValidity() !== true ||
    passwordEl.value?.reportValidity() !== true
  ) {
    return;
  }

  loading.value = true;
  button.loading = true;

  const action = button.dataset.action;

  try {
    switch (action) {
      case "sign_in":
        await onSignIn(emailEl.value.value, passwordEl.value.value);
        break;
      case "sign_up":
        await onSignUp(emailEl.value.value, passwordEl.value.value);
        break;
    }
    await router.push({ name: "index" });
  } catch (e) {
    handleError(e as ErrorMessage, action);
  } finally {
    loading.value = false;
    button.loading = false;
  }
};

const onSignIn = async (email: string, password: string) => {
  await signIn(email, password);
};

const onSignUp = async (email: string, password: string) => {
  await signUp(email, password);
};

const onContinueWith = async (e: MouseEvent) => {
  const button = e.target as Button;

  loading.value = true;
  button.loading = true;

  const action = button.dataset.action;

  try {
    switch (action) {
      case "passkey":
        await onContinueWithPasskey();
        break;
    }
    await router.push({ name: "index" });
  } catch (e) {
    handleError(e as ErrorMessage, action);
  } finally {
    loading.value = false;
    button.loading = false;
  }
};

const onContinueWithPasskey = async () => {
  await validatePasskeyCredential();
};

const activatePasskeyConditionalUI = async () => {
  if (isSupportedPasskey.value) {
    return;
  }

  isPasskeyConditionalUIRunning.value = true;

  const controller = new AbortController();
  onUnmounted(async () => {
    controller.abort({ code: -1 });
  });
  try {
    await validatePasskeyCredential(true, controller.signal, {
      beforeVerifyAssertion: () => {
        loading.value = true;
        passkeyEl.value!.loading = true;
      },
    });
    await router.push({ name: "index" });
  } catch (e) {
    if (e.code === -1) return; // ignore abort
    handleError(e as ErrorMessage, "passkey");
  } finally {
    isPasskeyConditionalUIRunning.value = false;
    loading.value = false;
    if (passkeyEl.value) {
      passkeyEl.value.loading = false;
    }
  }
};

onMounted(async () => {
  await activatePasskeyConditionalUI();
});
</script>

<template>
  <div class="outer-container">
    <div class="container">
      <div class="title">
        <n-text scale="display" size="large"
          >{{ t("page.sign_in.welcome") }}
        </n-text>
      </div>
      <div class="form">
        <mdui-text-field
          ref="emailEl"
          :label="t('email')"
          autocomplete="email webauthn"
          enterkeyhint="next"
          error-icon="error"
          icon="email"
          name="email"
          required
          type="email"
          variant="outlined"
        />
        <mdui-text-field
          ref="passwordEl"
          :label="t('password')"
          autocomplete="current-password"
          enterkeyhint="done"
          error-icon="error"
          icon="password"
          minlength="8"
          name="password"
          required
          toggle-password
          type="password"
          variant="outlined"
        />
        <div class="form--actions">
          <mdui-button
            :disabled="loading"
            data-action="sign_in"
            type="button"
            variant="filled"
            @click="onSubmit"
            >{{ t("sign_in") }}
          </mdui-button>
          <mdui-button
            :disabled="loading"
            data-action="sign_up"
            type="button"
            variant="tonal"
            @click="onSubmit"
            >{{ t("sign_up") }}
          </mdui-button>
        </div>
      </div>
      <div class="sign-in-with">
        <n-text>{{ t("page.sign_in.continue_with") }}</n-text>
        <mdui-divider class="divider" />
        <div class="sign-in-with--actions">
          <mdui-button
            ref="passkeyEl"
            :disabled="
              loading || !isSupportedPasskey || isPasskeyConditionalUIRunning
            "
            data-action="passkey"
            type="button"
            variant="tonal"
            @click="onContinueWith"
            >{{ t("passkey") }}
          </mdui-button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.outer-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 64px;
}

.container {
  padding: 0 32px;
}

.title {
  margin-bottom: 32px;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form--actions {
  display: flex;
  gap: 4px;
  justify-content: flex-end;
}

.sign-in-with {
  display: flex;
  flex-direction: column;

  margin-top: 32px;

  text-align: center;
}

.divider {
  margin: 8px 0;
}

.sign-in-with--actions {
  display: flex;
  gap: 4px;
  justify-content: center;
}
</style>
