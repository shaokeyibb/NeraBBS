<script lang="ts" setup>
import { useHead } from "@unhead/vue";
import { useI18n } from "vue-i18n";
import useLocale from "./hooks/locale.ts";
import useTheme from "./hooks/theme.ts";
import { useRoute } from "vue-router";
import { computed } from "vue";

const { t } = useI18n();
const { locale, syncLocale } = useLocale();
const { syncTheme } = useTheme();
const route = useRoute();

syncLocale();
syncTheme();

const titleTemplate = computed(
  () =>
    `%s %separator ${route.meta.title ? t(route.meta.title) : ""} %separator ${t("site.title")}`,
);

useHead({
  titleTemplate: titleTemplate,
  templateParams: {
    separator: "-",
  },
  htmlAttrs: {
    lang: locale.value,
  },
  link: [
    { rel: "preconnect", href: "https://fonts.googleapis.com" },
    {
      rel: "preconnect",
      href: "https://fonts.gstatic.com",
      crossorigin: "anonymous",
    },
    {
      rel: "stylesheet",
      href: "https://fonts.googleapis.com/icon?family=Material+Icons",
    },
    {
      rel: "stylesheet",
      href: "https://fonts.googleapis.com/icon?family=Material+Icons+Outlined",
    },
  ],
});
</script>

<template>
  <RouterView />
</template>

<style scoped></style>
