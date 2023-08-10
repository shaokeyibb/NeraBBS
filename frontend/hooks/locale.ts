export default function useLocale() {
    const {setLocale, messages} = useI18n()

    function getLocalizedLocalName(locale: string) {
        return `${messages.value[locale]['locale.name']} (${messages.value[locale]['locale.intl.name']})`
    }

    async function setLocalePermanently(locale: string) {
        await setLocale(locale)
    }

    return {
        getLocalizedLocalName,
        setLocalePermanently
    }
}