<script lang="ts" setup>
import useDarkMode from "~/hooks/dark_mode";
import useUser from "~/hooks/user";

const {$pinia} = useNuxtApp()

const {darkModeClass} = useDarkMode($pinia)

const {requestUserInfo} = useUser($pinia)
requestUserInfo()

watchEffect(() => {
  useHead({
    htmlAttrs: {
      class: [darkModeClass]
    }
  })
})

</script>

<template>
  <div class="dark:bg-black">
    <NuxtLayout>
      <NuxtPage/>
    </NuxtLayout>
  </div>
</template>