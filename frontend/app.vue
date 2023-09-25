<script lang="ts" setup>
import useDarkMode from "~/hooks/dark_mode";
import useUser from "~/hooks/user";

const {$pinia} = useNuxtApp()

const {darkModeClass} = useDarkMode($pinia)

const {requestUserInfo} = useUser($pinia)
requestUserInfo()

const {locale, setLocale, defaultLocale, t} = useI18n()

setLocale(useCookie("locale").value ?? defaultLocale)

watchEffect(() => {
  useHead({
    htmlAttrs: {
      class: [darkModeClass],
      lang: locale.value
    },
    title: t("app.name"),
  })
})

</script>
<template>
  <NuxtPage/>
</template>