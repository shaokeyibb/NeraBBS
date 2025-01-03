import {computedAsync} from "@vueuse/core";
import useBackend from "./backend.ts";

export default function usePost() {
  const { _getPost, _getPreviewPost } = useBackend();

  const getPost = (id: number) => computedAsync(() => _getPost(id));

  return {
    getPost,
    getPreviewPost: _getPreviewPost,
  };
}
