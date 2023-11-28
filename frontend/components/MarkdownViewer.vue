<script lang="ts" setup>
import 'bytemd/dist/index.css'
import 'highlight.js/styles/default.css'
import {Viewer} from '@bytemd/vue-next'
import type {BytemdPlugin} from "bytemd";
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight-ssr";
import breaks from "@bytemd/plugin-breaks";

const plugins: BytemdPlugin[] = [
  gfm(),
  highlight(),
  breaks()
]

defineProps<{
  value: string | undefined
}>()
</script>

<template>
  <Viewer :plugins="plugins"
          :value="value"/>
</template>

<style scoped>
:deep(.hljs) {
  background: transparent;
  color: unset;
}
</style>