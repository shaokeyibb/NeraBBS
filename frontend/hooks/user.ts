import {Pinia} from "pinia";
import {useUserInfoStore} from "~/stores/user_info";

export default function useUser(pinia: Pinia) {
    const store = useUserInfoStore(pinia)

    async function requestUserInfo() {
        try {
            store.userInfo = await $fetch("/api/users", {
                method: "GET",
                parseResponse: JSON.parse
            })
        } catch (e: any) {
            if (e.status === 401) {
                store.clearUserInfo()
                return
            }
            throw e
        }
    }

    async function signin(email: string, password: string) {
        await $fetch("/api/authorization/signin", {
            method: "POST",
            body: {
                email,
                password,
            }
        })
        await requestUserInfo()
    }

    async function signup(email: string, password: string) {
        await $fetch("/api/authorization/signup", {
            method: "POST",
            body: {
                email,
                password,
            }
        })
        await requestUserInfo()
    }

    async function signout() {
        const signout = $fetch("/api/authorization/signout", {
            method: "POST"
        })
        store.clearUserInfo()
        await signout
    }

    return {
        requestUserInfo,
        signin,
        signup,
        signout
    }
}