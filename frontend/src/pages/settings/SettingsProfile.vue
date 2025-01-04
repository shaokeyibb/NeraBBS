<script setup lang="ts">
import NCardMain from "../../components/NCardMain.vue";

const { insideCard = false } = defineProps<{
  insideCard?: boolean;
}>();
</script>

<template>
  <component :is="insideCard ? NCardMain : 'div'"> </component>
</template>

<style scoped></style>
