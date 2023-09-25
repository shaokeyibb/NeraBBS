<script lang="ts" setup>
import {NuxtLink} from "#components";
import {snackbar} from "halcyon-vue";
import useUser from "~/hooks/user";

const router = useRouter()
const {t} = useI18n()

const {$pinia} = useNuxtApp()
const {signup} = useUser($pinia)

const sizeClass = ref<'compact' | 'medium' | 'expanded'>('expanded')
const panes = ref<1 | 2>(1)

const email = ref()
const password = ref()

async function onSubmit(e: Event) {
  const target = e.target as HTMLFormElement
  try {
    await signup(
        target.email.value,
        target.password.value,
    )
    await router.push("/")
  } catch (e: any) {
    if (e.status === 400) {
      snackbar.send({
        message: t("signup.failed.bad_request")
      })
      return
    } else if (e.status === 403) {
      snackbar.send({
        message: t("signup.failed.user_already_exists")
      })
      return
    }
    snackbar.send({
      message: t("signup.failed.internal", {err: e.data.message})
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
      <HTopAppBar :title="$t('signup')" kind="small">
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
            t('signup.welcome.title', {"app_name": 'NeraBBS'})
          }}</h1>
        <h2 class="mt-1 text-xl lg:text-2xl text-center">
          {{ t('signup.welcome.subtitle', {"app_name": 'NeraBBS'}) }}</h2>
      </div>
      <div class="mx-auto lg:max-w-screen-sm">
        <form class="flex flex-col" method="post" @submit.prevent="onSubmit">
          <HTextField v-model="email"
                      :label="$t('signup.form.email.title')"
                      :placeholder="$t('signup.form.email.placeholder')"
                      :required="true"
                      :showRequired="false"
                      autocomplete="email"
                      inputType="email"
                      kind="outlined"
                      name="email"/>
          <HTextField v-model="password"
                      :label="$t('signup.form.password.title')"
                      :minlength="8"
                      :placeholder="$t('signup.form.password.placeholder')"
                      :required="true"
                      :showRequired="false"
                      autocomplete="new-password"
                      inputType="password"
                      kind="outlined"
                      name="password"/>
          <div class="flex flex-row lg:flex-col justify-between lg:items-end mt-2 flex-wrap">
            <HButton :as="NuxtLink" :content="$t('signup.to.signin')" kind="text" to="/signin" type="button"/>
          </div>
          <div class="flex flex-row justify-center mt-5">
            <HButton :content="$t('signup')" class="w-full min-w-screen" kind="filled" type="submit"/>
          </div>
        </form>
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