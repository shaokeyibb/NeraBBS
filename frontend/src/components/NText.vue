<script lang="ts" setup>
import {computed} from "vue";

const {
  scale = "body",
  size = "medium",
  as = "span",
  color = "text",
} = defineProps<{
  scale?: "display" | "headline" | "title" | "label" | "body";
  size?: "large" | "medium" | "small";
  color?: "primary" | "secondary" | "tertiary" | "error" | "text";
  as?: keyof HTMLElementTagNameMap;
}>();

defineSlots<{
  default: unknown;
}>();

const styles = computed(() => {
  return {
    lineHeight: `var(--mdui-typescale-${scale}-${size}-line-height)`,
    fontSize: `var(--mdui-typescale-${scale}-${size}-size)`,
    letterSpacing: `var(--mdui-typescale-${scale}-${size}-tracking)`,
    fontWeight: `var(--mdui-typescale-${scale}-${size}-weight)`,
    color: color === "text" ? undefined : `rgb(var(--mdui-color-${color}))`,
  };
});
</script>

<template>
  <component :is="as" :style="styles">
    <slot />
  </component>
</template>
