<script lang="ts" setup>
import {NuxtLink} from "#components";
import {snackbar} from "halcyon-vue";
import usePasskey from "~/hooks/passkey";
import useUser from "~/hooks/user";

const router = useRouter()
const {t} = useI18n()
const {validatePasskeyCredential} = usePasskey()

const {$pinia} = useNuxtApp()
const {requestUserInfo, signin} = useUser($pinia)

const sizeClass = ref<'compact' | 'medium' | 'expanded'>('expanded')
const panes = ref<1 | 2>(1)

const email = ref()
const password = ref()

async function onSubmit(e: Event) {
  const target = e.target as HTMLFormElement
  try {
    await signin(
        target.email.value,
        target.password.value,
    )
    await router.push("/")
  } catch (e: any) {
    if (e.status === 400) {
      snackbar.send({
        message: t("signin.failed.bad_request")
      })
      return
    } else if (e.status === 401) {
      snackbar.send({
        message: t("signin.failed.unauthorized")
      })
      return
    }
    snackbar.send({
      message: t("signin.failed.internal", {err: e})
    })
  }
}

async function onSignInWithPasskey() {
  try {
    await validatePasskeyCredential()
    await requestUserInfo()
    await router.push("/")
  } catch (e: any) {
    if (e.status === 401) {
      snackbar.send({
        message: t("signin.failed.unauthorized")
      })
      return
    }
    snackbar.send({
      message: t("signin.failed.internal", {err: e.data.message})
    })
  }
}

</script>

<template>
  <HScaffold v-model:panes="panes"
             v-model:sizeClass="sizeClass"
             :initial-panes="1"
             initialSizeClass="auto">
    <template #defaultContent>
      <HTopAppBar :title="$t('signin')" kind="small">
        <template #navigation-icon>
          <HIconButton :as="NuxtLink" :label="$t('home')" :noTooltip="true"
                       kind="standard"
                       to="/">
            <svg class="icon-[material-symbols--home]"/>
          </HIconButton>
        </template>
      </HTopAppBar>
      <div class="mb-5 lg:mb-10">
        <h1 class="text-2xl lg:text-4xl text-center">{{
            t('signin.welcome.title', {"app_name": 'NeraBBS'})
          }}</h1>
        <h2 class="mt-1 text-xl lg:text-2xl text-center">
          {{ t('signin.welcome.subtitle', {"app_name": 'NeraBBS'}) }}</h2>
      </div>
      <div class="mx-auto lg:max-w-screen-sm">
        <form class="flex flex-col" method="post" @submit.prevent="onSubmit">
          <HTextField v-model="email"
                      :label="$t('signin.form.email.title')"
                      :placeholder="$t('signin.form.email.placeholder')"
                      :required="true"
                      :showRequired="false"
                      autocomplete="email"
                      inputType="email"
                      kind="outlined"
                      name="email"/>
          <HTextField v-model="password"
                      :label="$t('signin.form.password.title')"
                      :minlength="8"
                      :placeholder="$t('signin.form.password.placeholder')"
                      :required="true"
                      :showRequired="false"
                      autocomplete="current-password"
                      inputType="password"
                      kind="outlined"
                      name="password"/>
          <div class="flex flex-row lg:flex-col justify-between lg:items-end mt-2 flex-wrap">
            <HButton :as="NuxtLink" :content="$t('signin.forget_password')" :disabled="true" kind="text" type="button"/>
            <HButton :as="NuxtLink" :content="$t('signin.to.signup')" kind="text" to="/signup" type="button"/>
          </div>
          <div class="flex flex-row justify-center mt-5">
            <HButton :content="$t('signin')" class="w-full min-w-screen" kind="filled" type="submit"/>
          </div>
        </form>
        <div class="px-3 py-7 relative">
          <HDivider/>
          <span class="absolute text-sm top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 bg-halcyon-surface px-2">{{
              $t("signin.with")
            }}</span>
        </div>
        <div class="grid grid-cols-2 lg:grid-cols-3">
          <HButton :content="$t('signin.with.passkey')" kind="filled-tonal" @click="onSignInWithPasskey">
            <svg class="icon-[material-symbols--passkey] h-6 w-6"/>
          </HButton>
        </div>
      </div>
    </template>
  </HScaffold>

  <HSnackbar align="center"/>
</template>

<style scoped>
.w-full.min-w-screen.button {
  width: 100%;
  min-width: 100%;
}
</style>