import {UserInfo} from "~/data/common";

export const useUserInfoStore = defineStore('user-info', () => {
    const userInfo = ref<UserInfo>()

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