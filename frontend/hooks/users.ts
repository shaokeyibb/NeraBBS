import type {UserInfo, UserProfile} from "~/data/common";

export default function useUsers() {

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

    return {
        getUserInfo,
        getUserProfile
    }

}