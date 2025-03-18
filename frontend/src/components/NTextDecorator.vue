<script setup lang="ts">
import { computed } from "vue";

const { text } = defineProps<{
  text: string;
}>();

const parsedParts = computed(() => {
  const regex = /<em>(.*?)<\/em>/g;
  const parts: Array<{ text: string; bold: boolean }> = [];
  let lastIndex = 0;
  let match;

  while ((match = regex.exec(text)) !== null) {
    // Add text before match
    if (match.index > lastIndex) {
      parts.push({
        text: text.slice(lastIndex, match.index),
        bold: false,
      });
    }
    // Add matched text in bold
    parts.push({
      text: match[1],
      bold: true,
    });
    lastIndex = regex.lastIndex;
  }

  // Adding remaining text if any
  if (lastIndex < text.length) {
    parts.push({
      text: text.slice(lastIndex),
      bold: false,
    });
  }

  return parts;
});
</script>

<template>
  <template v-for="(part, index) in parsedParts" :key="index">
    <em v-if="part.bold">{{ part.text }}</em>
    <span v-else>{{ part.text }}</span>
  </template>
</template>

<style scoped>
em {
  font-weight: bold;
}
</style>
