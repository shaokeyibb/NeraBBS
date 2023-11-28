import type {UserInfo, UserProfile} from "~/data/common";

export default function useUsers() {

    const cachedUserProfiles = reactive<Record<number, {
        pending: boolean,
        error?: Error,
        data?: UserProfile
    }>>({})

    async function getUserInfo(id: number): Promise<UserInfo> {
        return await $fetch(`/api/users/${id}`, {
            method: "GET",
            parseResponse: JSON.parse
        })
    }

    async function getUserProfile(id: number): Promise<UserProfile> {
        return await $fetch(`/api/users/${id}/profile`, {
            method: "GET",
            parseResponse: JSON.parse
        })
    }

    function getCachedUserProfile(id: number) {
        if (!cachedUserProfiles[id]) {
            requireUserProfile(id)
        }
        return cachedUserProfiles[id]
    }

    async function requireUserProfile(id: number) {
        const data = getUserProfile(id)
        cachedUserProfiles[id] = {
            pending: true,
        }
        try {
            cachedUserProfiles[id].data = (await data)
        } catch (e: any) {
            cachedUserProfiles[id].error = e
        } finally {
            cachedUserProfiles[id].pending = false
        }
        return data
    }

    return {
        getUserInfo,
        getUserProfile,
        getCachedUserProfile,
    }

}