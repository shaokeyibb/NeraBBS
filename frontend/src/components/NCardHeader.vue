<script lang="ts" setup>
import {computed, inject, onMounted, ref} from "vue";
import {isInNCard} from "../utils/symbol.ts";
import NText from "./NText.vue";

const {header, subhead} = defineProps<{
  header?: string;
  subhead?: string;
}>();

const {monogram, button} = defineSlots<{
  monogram: unknown;
  button: unknown;
}>();

const isInCard = inject(isInNCard, false);

const cardHeader = ref<HTMLElement>();

const paddingRight = computed(() => {
  return button ? "4px" : "12px";
});

onMounted(() => {
  setTimeout(() => {
    if (isInCard && cardHeader.value) {
      cardHeader.value.style.padding = `16px ${paddingRight.value} 16px 12px`;
    }
  }, 0);
});
</script>

<template>
  <div ref="cardHeader" class="card--header">
    <div class="card--header--content">
      <slot
          v-if="monogram"
          class="card--header--content-monogram"
          name="monogram"
      ></slot>
      <div v-if="header || subhead" class="card--header--content--text">
        <NText v-if="header" class="header" scale="title">{{ header }}</NText>
        <NText v-if="subhead" class="subhead" scale="body">{{ subhead }}</NText>
      </div>
    </div>
    <span class="spacer"></span>
    <div v-if="button" class="card--header--button">
      <slot name="button"></slot>
    </div>
  </div>
</template>

<style scoped>
.card--header {
  display: flex;
  flex-direction: row;
  align-items: center;

  padding: 16px v-bind(paddingRight) 16px 12px;
}

.card--header--content {
  display: flex;
  flex-direction: row;

  gap: 16px;
}

.card--header--content--text {
  display: flex;
  flex-direction: column;

  overflow: hidden;
  text-overflow: ellipsis;
}

.spacer {
  flex: 1;
}
</style>
