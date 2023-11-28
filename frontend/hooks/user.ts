import {Pinia} from "pinia";
import {useUserInfoStore, useUserProfileStore} from "~/stores/user";
import useUsers from "~/hooks/users";
import type {UserInfo, UserProfile, UserProfilePatchReq} from "~/data/common";

export default function useUser(pinia: Pinia) {
    const users = useUsers()
    const userInfoStore = useUserInfoStore(pinia)
    const userProfileStore = useUserProfileStore(pinia)

    async function requestUser() {
        try {
            await requestUserInfo()
            await requestUserProfile(userInfoStore.userInfo!.id)
        } catch (e: any) {
            if (e.status === 401) {
                userInfoStore.clearUserInfo()
                userProfileStore.clearUserProfile()
                return
            }
            throw e
        }
    }

    async function requestUserInfo() {
        userInfoStore.userInfo = await $fetch<UserInfo>("/api/users", {
            method: "GET",
            parseResponse: JSON.parse
        })
    }

    async function requestUserProfile(id: number) {
        userProfileStore.userProfile = await users.getUserProfile(id)
    }

    async function signin(email: string, password: string) {
        await $fetch("/api/authorization/signin", {
            method: "POST",
            body: {
                email,
                password,
            }
        })
        await requestUser()
    }

    async function signup(email: string, password: string) {
        await $fetch("/api/authorization/signup", {
            method: "POST",
            body: {
                email,
                password,
            }
        })
        await requestUser()
    }

    async function signout() {
        const signout = $fetch("/api/authorization/signout", {
            method: "POST"
        })
        userInfoStore.clearUserInfo()
        userProfileStore.clearUserProfile()
        await signout
    }

    async function updateUserProfile(profile: UserProfilePatchReq) {
        const data = new FormData()
        for (const name in profile) {
            // @ts-ignore
            data.append(name, profile[name]);
        }

        userProfileStore.userProfile = await $fetch<UserProfile>(`/api/users/${profile.userID}/profile`, {
            method: "PATCH",
            body: data,
            parseResponse: JSON.parse
        })
    }

    return {
        requestUserInfo: requestUser,
        signin,
        signup,
        signout,
        updateUserProfile
    }
}