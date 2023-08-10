type user_info = {
    id: number,
    username: string,
    email: string
}

export const useUserInfoStore = defineStore('user-info', () => {
    const userInfo = ref<user_info>()

    function clearUserInfo() {
        userInfo.value = undefined
    }

    return {
        userInfo,
        clearUserInfo
    }
}, {
    persist: {
        storage: persistedState.sessionStorage
    },
})