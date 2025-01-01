import { loadLocale } from "mdui";
import type { Plugin } from "vue";

const mdui: Plugin = {
  install() {
    loadLocale(
      (locale) => import(`../../node_modules/mdui/locales/${locale}.js`),
    );
  },
};

export default mdui;
