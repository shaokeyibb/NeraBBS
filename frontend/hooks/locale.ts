export default function useLocale() {
    const {setLocale, messages} = useI18n()

    function getLocalizedLocalName(locale: string) {
        return `${messages.value[locale]['locale.name']} (${messages.value[locale]['locale.intl.name']})`
    }

    function setLocalePermanently(locale: string) {
        setLocale(locale)
    }

    return {
        getLocalizedLocalName,
        setLocalePermanently
    }
}