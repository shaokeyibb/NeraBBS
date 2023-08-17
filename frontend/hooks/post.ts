import {PostCreation, PreviewPost} from "~/data/common";

export default function usePost() {
    async function publishPost(data: PostCreation): Promise<number> {
        return await $fetch<number>("/api/posts", {
            method: "POST",
            body: data,
        })
    }

    async function getPreviewPosts(page: number, size: number = 20): Promise<PreviewPost[]> {
        return await $fetch<PreviewPost[]>("/api/posts", {
            method: "GET",
            query: {
                page,
                size,
            },
            parseResponse: JSON.parse
        })
    }

    return {
        publishPost,
        getPreviewPosts
    }
}