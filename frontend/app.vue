<script lang="ts" setup>
import {ofetch} from 'ofetch'
import useDarkMode from "~/hooks/dark_mode";

const router = useRouter()

globalThis.$fetch = ofetch.create({
  onResponseError: ({response: {status}}) => {
    status === 401 && router.push("/signin")
  }
})

const {$pinia} = useNuxtApp()

const {darkModeClass} = useDarkMode($pinia)

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