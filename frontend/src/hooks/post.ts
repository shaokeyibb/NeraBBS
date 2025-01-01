import { computedAsync, useMemoize } from "@vueuse/core";
import useBackend from "./backend.ts";

export default function usePost() {
  const { _getPost, _getPreviewPost } = useBackend();

  const _getPostMemorized = useMemoize(_getPost);

  const getPost = (id: number) => computedAsync(() => _getPostMemorized(id));

  return {
    getPost,
    getPreviewPost: _getPreviewPost,
  };
}
