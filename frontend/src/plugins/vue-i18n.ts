import { createI18n } from "vue-i18n";
import { fallbackLocale, messages } from "../utils/locale.ts";

const i18n = createI18n({
  legacy: false,
  fallbackLocale,
  flatJson: true,
  messages,
});

export default i18n;
