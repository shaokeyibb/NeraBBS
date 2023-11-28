import type {Post, PostCreation, PreviewPost} from "~/data/common";

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

    async function getPost(id: string): Promise<Post> {
        return await $fetch<Post>(`/api/posts/${id}`, {
            method: "GET",
            parseResponse: JSON.parse
        })
    }

    return {
        publishPost,
        getPreviewPosts,
        getPost
    }
}