<script lang="ts" setup>
import useUser from "~/hooks/user";

const router = useRouter()
const {t} = useI18n()

const {$pinia} = useNuxtApp()
const {signup} = useUser($pinia)

async function onSubmit(e: Event) {
  const target = e.target as HTMLFormElement
  try {
    await signup(
        target.username.value,
        target.email.value,
        target.password.value,
    )
    await router.push("/")
  } catch (e: any) {
    if (e.status === 400) {
      alert(t("signup.error.bad_request"))
      return
    }
    alert(t("signup.error.internal", {err: e.data.message}))
  }
}
</script>

<template>
  <div class="container mx-auto my-3 px-4 py-6">
    <div class="pb-10">
      <h1 class="text-2xl lg:text-4xl dark:text-white text-center">{{ t('signup.welcome', {appName: 'NeraBBS'}) }}</h1>
      <h2 class="text-xl lg:text-2xl dark:text-white text-center">{{
          t('signup.welcome.subtitle', {appName: 'NeraBBS'})
        }}</h2>
    </div>
    <form class="flex flex-col gap-6 w-[340px] mx-auto" method="post" @submit.prevent="onSubmit">
      <label class="flex flex-col dark:text-white gap-2">
        {{ $t('username') }}
        <input autocomplete="nickname" class="dark:bg-black" minlength="3" name="username" pattern="(.|\s)*\S(.|\s)*" required
               type="text"/>
      </label>
      <label class="flex flex-col dark:text-white gap-2">
        {{ $t('email') }}
        <input autocomplete="email" class="dark:bg-black" name="email" required type="email"/>
      </label>
      <label class="flex flex-col dark:text-white gap-2">
        {{ $t('password') }}
        <input autocomplete="new-password" class="dark:bg-black" minlength="8" name="password" required
               type="password"/>
      </label>
      <button class="border rounded py-3 px-5 dark:text-white bg-secondary dark:bg-black" type="submit">{{
          $t('sign_up')
        }}
      </button>
    </form>
    <div class="mt-10 text-center text-tertiary">
      <NuxtLink to="/signin">{{ $t('signup.to.signin') }}</NuxtLink>
    </div>
  </div>
</template>