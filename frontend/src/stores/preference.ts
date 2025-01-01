import { defineStore } from "pinia";
import { ref } from "vue";

export const usePreferenceStore = defineStore(
  "preference",
  () => {
    const locale = ref<string>();
    const theme = ref<"dark" | "light" | "auto">("auto");

    return {
      locale,
      theme,
    };
  },
  {
    persist: true,
  },
);
