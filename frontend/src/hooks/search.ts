import useBackend from "./backend.ts";
import type { Post } from "../types/backend.ts";

export default function useSearch() {
  const { _search, _getHitCount, _getTrending } = useBackend();

  const search = async <T>(index: string, query: string, page: number) => {
    return await _search<T>(index, query, page * 20, 20);
  };

  const searchPost = async (query: string, page: number) => {
    return await search<Post>("posts", query, page);
  };

  const getHitCount = async (topic: string, key: string) => {
    return await _getHitCount(topic, key);
  };

  const getTrending = async (topic: string) => {
    return await _getTrending(topic, 10);
  };

  const getPostHitCount = async (key: string) => {
    return await getHitCount("posts", key);
  };

  const getPostTrending = async () => {
    return await getTrending("posts");
  };

  return {
    searchPost,
    getPostHitCount,
    getPostTrending,
  };
}
