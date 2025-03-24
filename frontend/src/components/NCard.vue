<script lang="ts" setup>
import { onMounted, provide, type Ref, ref, useTemplateRef } from "vue";
import { isInNCard } from "../utils/symbol.ts";

const {
  variant = "filled",
  clickable = false,
  disabled = false,
} = defineProps<{
  variant?: "elevated" | "filled" | "outlined";
  clickable?: boolean;
  disabled?: boolean;
}>();

const {
  default: _default,
  header,
  media,
} = defineSlots<{
  default: unknown;
  header: unknown;
  media: unknown;
}>();

provide(isInNCard, true);

const card = useTemplateRef<HTMLElement>("card");
const headerEl = ref<HTMLElement>();
const contentEl = ref<HTMLElement>();

const removeDuplicateContainer = (el: Ref<HTMLElement | undefined>) => {
  if (el.value == undefined || card.value == undefined) return;
  if (el.value.children.length !== 1) {
    console.error(
      "NCard children element must have exactly one child.",
      el.value,
    );
  }
  const innerEl = el.value.firstElementChild!;
  card.value.insertBefore(innerEl, el.value.nextSibling);
  el.value!.remove();
  headerEl.value = innerEl as HTMLElement;
};

onMounted(() => {
  if (header) {
    setTimeout(() => {
      removeDuplicateContainer(headerEl);
      removeDuplicateContainer(contentEl);
    }, 0);
  }
});
</script>

<template>
  <mdui-card
    ref="card"
    :clickable="clickable"
    :variant="variant"
    :disabled="disabled"
    class="card"
  >
    <div v-if="header" ref="headerEl" class="card--header">
      <slot name="header" />
    </div>
    <div v-if="media" class="card--media">
      <slot name="media" />
    </div>
    <div v-if="_default" ref="contentEl" class="card--content">
      <slot name="default" />
    </div>
  </mdui-card>
</template>

<style scoped>
.card {
  width: 100%;
}

.card--header {
  padding: 16px 12px;
}

.card--media {
  width: 100%;
}

.card--media img {
  width: 100%;
}

.card--content {
  padding: 16px;
}
</style>
