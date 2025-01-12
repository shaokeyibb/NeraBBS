<script setup lang="ts">
import { VueMarkdownIt } from "@f3ve/vue-markdown-it";

const { title, content } = defineProps<{
  title?: string;
  content: string;
}>();
</script>

<template>
  <div class="mdui-prose">
    <h1>{{ title }}</h1>
    <vue-markdown-it :source="content" />
  </div>
</template>

<style scoped></style>
