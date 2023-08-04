<script lang="ts" setup>
import AppHeader from "~/components/AppHeader.vue";
import AppFooter from "~/components/AppFooter.vue";

const header = ref<Element>()
const _headerHeight = ref<number>()
const headerHeight = computed(() => `${_headerHeight.value}px`)

onMounted(() => {
  _headerHeight.value = header.value?.clientHeight
})
</script>

<template>
  <div class="flex flex-col min-h-screen">
    <header ref="header" class="fixed w-full flex-shrink-0">
      <AppHeader/>
    </header>
    <main class="flex-auto">
      <slot/>
    </main>
    <footer class="w-full">
      <AppFooter/>
    </footer>
  </div>
</template>

<style>
main {
  padding-top: v-bind(headerHeight);
}
</style>