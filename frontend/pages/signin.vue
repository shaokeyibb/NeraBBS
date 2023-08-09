<script lang="ts" setup>
const router = useRouter()
const {t} = useI18n()

async function onSubmit(e: Event) {
  const target = e.target as HTMLFormElement
  try {
    await $fetch("/api/authorization/signin", {
      method: "POST",
      body: {
        email: target.email.value,
        password: target.password.value,
      }
    })
    await router.push("/")
  } catch (e: any) {
    alert(t("signin.error.internal", {err: e.message}))
  }
}
</script>

<template>
  <div class="container mx-auto my-3 px-4 py-6">
    <div class="pb-10">
      <h1 class="text-2xl lg:text-4xl dark:text-white text-center">{{ t('signin.welcome', {appName: 'NeraBBS'}) }}</h1>
      <h2 class="text-xl lg:text-2xl dark:text-white text-center">{{
          t('signin.welcome.subtitle', {appName: 'NeraBBS'})
        }}</h2>
    </div>
    <form class="flex flex-col gap-6 w-[340px] mx-auto" method="post" @submit.prevent="onSubmit">
      <label class="flex flex-col dark:text-white gap-2">
        {{ $t('email') }}
        <input class="dark:bg-black" name="email" required type="email"/>
      </label>
      <label class="flex flex-col dark:text-white gap-2">
        {{ $t('password') }}
        <input class="dark:bg-black" minlength="8" name="password" required type="password"/>
      </label>
      <button class="border rounded py-3 px-5 dark:text-white bg-secondary dark:bg-black" type="submit">{{
          $t('sign_in')
        }}
      </button>
    </form>
  </div>
</template>