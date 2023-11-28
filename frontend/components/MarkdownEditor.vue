<script lang="ts" setup>
import 'bytemd/dist/index.css'
import 'highlight.js/styles/default.css'
import {Editor} from '@bytemd/vue-next'

import application_locale_en_us from "~/locales/en_us";
import application_locale_zh_cn from "~/locales/zh_cn";

import bytemd_locale_zh_Hans from 'bytemd/locales/zh_Hans.json'
import bytemd_locale_en from 'bytemd/locales/en.json'

import gfm from "@bytemd/plugin-gfm";
import highlight from '@bytemd/plugin-highlight-ssr'
import breaks from '@bytemd/plugin-breaks'
import {BytemdPlugin} from "bytemd";

const locales = reactive<{
  [key: string]: typeof bytemd_locale_zh_Hans & Partial<{
    strike: string;
    strikeText: string;
    task: string;
    taskText: string;
    table: string;
    tableHeading: string;
  }>
}>({
  'en-US': {
    ...bytemd_locale_en,
    ...application_locale_en_us,
  },
  'zh-CN': {
    ...bytemd_locale_zh_Hans,
    ...application_locale_zh_cn,
  },
})

const locale = computed(() => locales[useI18n().locale.value] || locales['en-US'])

const plugins: BytemdPlugin[] = [
  gfm({locale: locale.value}),
  highlight(),
  breaks()
]

withDefaults(defineProps<{
  showTitle?: boolean,
  modelValue: string | undefined,
  title?: string | undefined,
  placeholder?: string,
  titlePlaceholder?: string,
  maxLength?: number,
  maxTitleLength?: number,
  height?: string,
  titleHeight?: string
}>(), {
  showTitle: false,
  maxLength: 65535,
  maxTitleLength: 255,
  height: '300px',
  titleHeight: 'auto',
})

defineEmits<{
  (e: 'update:modelValue', value: string | undefined): void
  (e: 'update:title', value: string | undefined): void
}>()

defineSlots<{
  actions(): any
}>()
</script>

<template>
  <div class="flex flex-row border-t border-x border-[#e1e4e8]">
    <input v-if="showTitle"
           :maxlength="maxTitleLength"
           :placeholder="titlePlaceholder"
           :value="title"
           autocomplete="on"
           class="flex-auto dark:bg-black dark:text-white border-0 border-r border-r-[#e1e4e8]"
           type="text"
           @input="$emit('update:title',$event.target.value)"/>
    <slot name="actions"></slot>
  </div>
  <Editor :locale="locale"
          :maxLength="maxLength"
          :placeholder="placeholder"
          :plugins="plugins"
          :value="modelValue"
          @change="(v) => $emit('update:modelValue',v)"/>
  <!--  uploadImages?: (files: File[]) => Promise<Pick<Image, 'url' | 'alt' | 'title'>[]>-->
</template>

<style scoped>
input {
  height: v-bind(titleHeight);
}

input::placeholder {
  color: #9CA3AF;
}

:deep(.bytemd) {
  height: v-bind(height);
}

:deep(.hljs) {
  background: transparent;
}
</style>

<style>
html.dark .bytemd {
  background-color: black;
}

html.dark .bytemd .bytemd-toolbar {
  color: #9CA3AF;
  background-color: black;
}

html.dark .bytemd .bytemd-toolbar-icon {
  color: #9CA3AF;
}

html.dark .bytemd .bytemd-status {
  color: #9CA3AF;
}

html.dark .CodeMirror {
  background-color: black;
}

html.dark .bytemd .bytemd-preview {
  color: white;
}

html.dark .CodeMirror {
  color: white;
}

html.dark .CodeMirror .CodeMirror-cursor {
  border-color: white;
}
</style>