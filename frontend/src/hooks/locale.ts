import { useI18n } from "vue-i18n";
import { setLocale as setMDUILocale } from "mdui/functions/setLocale.js";
import { storeToRefs } from "pinia";
import { usePreferenceStore } from "../stores/preference.ts";
import { customRef } from "vue";
import {
  tryToConvertLocaleToMDUILocaleCode,
  tryToMatchExistingLocale,
} from "../utils/locale.ts";

export default function useLocale() {
  const { locale: preferenceLocale } = storeToRefs(usePreferenceStore());

  const { locale: i18Locale } = useI18n();

  // 更新语言设置
  const updateLocale = async (locale: string) => {
    i18Locale.value = tryToMatchExistingLocale(locale);
    await setMDUILocale(tryToConvertLocaleToMDUILocaleCode(locale));
  };

  // 同步语言设置为用户偏好（如果有）或浏览器语言
  const syncLocale = async () => {
    await updateLocale(preferenceLocale.value ?? navigator.language);
  };

  // 设置语言并更新用户偏好
  const setLocale = async (locale: string) => {
    await updateLocale(locale);
    preferenceLocale.value = locale;
  };

  const locale = customRef<string>((track, trigger) => {
    const get = () => {
      track();
      return i18Locale.value;
    };

    const set = async (value: string) => {
      await setLocale(value);
      trigger();
    };

    return { get, set };
  });

  return {
    locale,
    syncLocale,
    setLocale,
  };
}
