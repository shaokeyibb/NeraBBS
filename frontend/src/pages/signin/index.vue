<script lang="ts" setup>
import NText from "../../components/NText.vue";
import type { TextField } from "mdui/components/text-field.js";
import type { Button } from "mdui/components/button.js";

import { useI18n } from "vue-i18n";
import useErrorHandling from "../../hooks/error-handling.ts";
import type { ErrorMessage } from "../../types/error-handling.ts";
import useAuth from "../../hooks/auth.ts";
import { useRouter } from "vue-router";
import { onMounted, ref, useTemplateRef } from "vue";
import { isSupported as _isSupportedPasskey } from "../../utils/passkey.ts";
import { computedAsync } from "@vueuse/core";
import usePasskey from "../../hooks/passkey.ts";

const { t } = useI18n();
const { handle: handleError } = useErrorHandling();
const { signIn, signUp, isLoggedIn } = useAuth();
const { validatePasskeyCredential } = usePasskey();
const router = useRouter();

const emailEl = useTemplateRef<TextField>("emailEl");
const passwordEl = useTemplateRef<TextField>("passwordEl");

const loading = ref(false);
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
      case "continue_with_passkey":
        await onContinueWithPasskey();
        break;
    }
    await router.push({ name: "index" });
  } catch (e) {
    handleError(e as ErrorMessage);
  } finally {
    loading.value = false;
    button.loading = false;
  }
};

const onContinueWithPasskey = async () => {
  await validatePasskeyCredential();
};

onMounted(async () => {
  if (!(await isLoggedIn())) return;
  await router.push({ name: "index" });
});
</script>

<template>
  <div class="outer-container">
    <div class="container">
      <div class="title">
        <n-text scale="display" size="large"
          >{{ t("page.signIn.welcome") }}
        </n-text>
      </div>
      <div class="form">
        <mdui-text-field
          ref="emailEl"
          :label="t('email')"
          autocomplete="email"
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
            >{{ t("signIn") }}
          </mdui-button>
          <mdui-button
            :disabled="loading"
            data-action="sign_up"
            type="button"
            variant="tonal"
            @click="onSubmit"
            >{{ t("signUp") }}
          </mdui-button>
        </div>
      </div>
      <div class="sign-in-with">
        <n-text>{{ t("page.signIn.continue_with") }}</n-text>
        <mdui-divider class="divider" />
        <div class="sign-in-with--actions">
          <mdui-button
            :disabled="loading || !isSupportedPasskey"
            data-action="continue_with_passkey"
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
