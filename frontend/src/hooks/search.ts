import useBackend from "./backend.ts";
import type { Post } from "../types/backend.ts";

export default function useSearch() {
  const { _search } = useBackend();

  const search = async <T>(index: string, query: string, page: number) => {
    return await _search<T>(index, query, page * 20, 20);
  };

  const searchPost = async (query: string, page: number) => {
    return await search<Post>("posts", query, page);
  };

  return {
    searchPost,
  };
}
