import type {UserInfo, UserProfile} from "~/data/common";

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

export const useUserProfileStore = defineStore('user-profile', () => {
    const userProfile = ref<UserProfile>()

    function clearUserProfile() {
        userProfile.value = undefined
    }

    return {
        userProfile,
        clearUserProfile
    }
})