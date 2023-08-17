import {UserInfo} from "~/data/common";

export default function useUsers() {

    async function getUserInfo(id: number): Promise<UserInfo> {
        return await $fetch(`/api/users/${id}`, {
            method: "GET",
            parseResponse: JSON.parse
        })
    }

    return {
        getUserInfo
    }

}