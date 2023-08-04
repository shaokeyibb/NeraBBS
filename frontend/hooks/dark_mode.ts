import {Pinia} from "pinia";
import {useDarkModeStore} from "~/stores/dark_mode";
import {useMediaQuery} from "@vueuse/core";

export default function useDarkMode(pinia: Pinia) {
    const store = useDarkModeStore(pinia)

    const darkModeClass = computed(() => {
        const systemDark = useMediaQuery('(prefers-color-scheme: dark)')

        if (store.mode === 'dark' || (store.mode === "auto" && systemDark.value)) {
            return 'dark'
        } else {
            return ''
        }
    })

    function switchToLightMode() {
        store.switchMode('light')
    }

    function switchToDarkMode() {
        store.switchMode('dark')
    }

    function switchToAuto() {
        store.switchMode("auto")
    }

    function toggleDarkMode() {
        switch (store.mode) {
            case 'light':
                switchToDarkMode()
                break
            case 'dark':
                switchToAuto()
                break
            case 'auto':
                switchToLightMode()
        }
    }

    return {
        mode: computed(() => store.mode),
        toggleDarkMode,
        darkModeClass
    }

}