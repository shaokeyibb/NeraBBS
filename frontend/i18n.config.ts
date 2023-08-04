import en_us from "~/locales/en_us";
import zh_cn from "~/locales/zh_cn";

export default defineI18nConfig(() => ({
    legacy: false,
    locale: 'zh-CN',
    fallbackLocale: 'en-US',
    messages: {
        'en-US': en_us,
        'zh-CN': zh_cn,
    }
}))