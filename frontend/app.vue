<script lang="ts" setup>
import useDarkMode from "~/hooks/dark_mode";

const {$pinia} = useNuxtApp()

const {darkModeClass} = useDarkMode($pinia)

watchEffect(() => {
  useHead({
    bodyAttrs: {
      class: [darkModeClass]
    },
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