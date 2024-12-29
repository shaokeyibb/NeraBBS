import {computedAsync, createFetch, useMemoize} from "@vueuse/core";
import {apiBaseUrl} from "../config.ts";
import type {Post, PreviewPost, UserProfile} from "../types/backend.ts";
import type {ErrorMessage} from "../types/error-handling.ts";

const $fetch = createFetch({
    baseUrl: apiBaseUrl,
    options: {
        immediate: false,
        onFetchError: (ctx) => {
            ctx.error = {
                code: ctx.response?.status ?? ctx.error.code ?? -1,
                message: ctx.data?.message ?? ctx.error.message ?? "Internal error",
            } as ErrorMessage;

            return ctx;
        },
    },
});

export default function useBackend() {
    const _getUserProfile = async (id?: number) => {
        const res = $fetch(`users/${id === undefined ? "-1" : `${id}`}/profile`)
            .get()
            .json<UserProfile>();
        await res.execute(true);
        return res.data.value!;
    };

    const _getUserProfileMemorized = useMemoize(_getUserProfile, {
        getKey: (id) => id ?? -1,
    });

    const getUserProfile = (id?: number) => {
        return computedAsync(() => _getUserProfileMemorized(id));
    };

    const _getPost = async (id: number) => {
        const res = $fetch(`posts/${id}`).get().json<Post>();
        await res.execute(true);
        return res.data.value!;
    };

    const _getPostMemorized = useMemoize(_getPost);

    const getPost = (id: number) => computedAsync(() => _getPostMemorized(id));

    const getPreviewPost = async (page: number, size: number) => {
        const res = $fetch(`posts?page=${page}&size=${size}`)
            .get()
            .json<PreviewPost[]>();
        await res.execute(true);
        return res.data.value!;
    };

    return {
        getUserProfile,
        getPost,
        getPreviewPost,
    };
}
