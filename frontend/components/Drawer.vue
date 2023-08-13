<script lang="ts" setup>
defineSlots<{
  default(): any
}>()

const props = withDefaults(defineProps<{
  isOpen: boolean,
  zIndex?: number,
}>(), {
  zIndex: 10
})

const emit = defineEmits<{
  (e: 'update:isOpen', open: boolean): void
}>()

function close() {
  emit('update:isOpen', false)
}
</script>

<template>
  <div v-if="isOpen" id="drawer">
    <div id="drawer__content" class="fixed top-0 left-0 w-[75vw] h-screen bg-white dark:bg-black">
      <slot/>
    </div>
    <div id="drawer__mask" class="fixed top-0 left-0 w-screen h-screen bg-gray-400 bg-opacity-60" @click="close"/>
  </div>
</template>

<style scoped>
#drawer {
  z-index: v-bind(zIndex);
}

#drawer__content {
  z-index: calc(v-bind(zIndex) + 2);
}

#drawer__mask {
  z-index: calc(v-bind(zIndex) + 1);
}
</style>