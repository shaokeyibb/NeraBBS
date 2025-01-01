import { storeToRefs } from "pinia";
import { usePreferenceStore } from "../stores/preference.ts";
import { customRef } from "vue";
import { setTheme as setMDUITheme } from "mdui/functions/setTheme.js";

export default function useTheme() {
  const { theme: preferenceTheme } = storeToRefs(usePreferenceStore());

  const updateTheme = async (theme: "dark" | "light" | "auto") => {
    setMDUITheme(theme);
  };

  const syncTheme = async () => {
    await updateTheme(preferenceTheme.value);
  };

  const setTheme = async (theme: "dark" | "light" | "auto") => {
    await updateTheme(theme);
    preferenceTheme.value = theme;
  };

  const toggleTheme = async () => {
    switch (preferenceTheme.value) {
      case "light":
        await setTheme("dark");
        break;
      case "dark":
        await setTheme("auto");
        break;
      case "auto":
        await setTheme("light");
        break;
    }
  };

  const theme = customRef<"dark" | "light" | "auto">((track, trigger) => {
    const get = () => {
      track();
      return preferenceTheme.value;
    };

    const set = async (value: "dark" | "light" | "auto") => {
      await setTheme(value);
      trigger();
    };

    return { get, set };
  });

  return {
    theme,
    syncTheme,
    setTheme,
    toggleTheme,
  };
}
