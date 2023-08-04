export default function useLocale() {
    const {setLocale, setLocaleCookie, messages} = useI18n()

    function getLocalizedLocalName(locale: string) {
        return `${messages.value[locale]['locale.name']} (${messages.value[locale]['locale.intl.name']})`
    }

    function setLocalePermanently(locale: string) {
        setLocaleCookie(locale)
        setLocale(locale)
    }

    return {
        getLocalizedLocalName,
        setLocalePermanently
    }
}