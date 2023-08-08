export const useDarkModeStore = defineStore('dark_mode', () => {
    const mode = ref<"light" | "dark" | "auto">("auto")

    function switchMode(pMode: "light" | "dark" | "auto") {
        mode.value = pMode
    }

    return {mode, switchMode}
}, {
    persist: {
        storage: persistedState.cookies
    },
})