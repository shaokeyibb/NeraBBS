import {PostCreation} from "~/data/common";

export default function usePublish() {
    async function publishPost(data: PostCreation): Promise<number> {
        return await $fetch<number>("/api/posts", {
            method: "POST",
            body: data,
        })
    }

    return {
        publishPost
    }
}