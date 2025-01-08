<script setup lang="ts">
import NCardMain from "../../components/NCardMain.vue";

const { insideCard = false } = defineProps<{
  insideCard?: boolean;
}>();
</script>

<template>
  <component :is="insideCard ? NCardMain : 'div'" class="list"> </component>
</template>

<style scoped>
.list {
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.list-headline {
  display: flex;
  flex-direction: column;
  gap: 1px;
}
</style>
