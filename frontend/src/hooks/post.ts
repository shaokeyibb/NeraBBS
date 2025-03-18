import useBackend from "./backend.ts";
import type { Post } from "../types/backend.ts";

export default function usePost() {
  const { _getPost, _getPreviewPost, _createPost, _deletePost } = useBackend();

  function getPost(id: number): Promise<Post>;
  function getPost(id: number, pure: boolean): Promise<Post>;
  function getPost(id: number, pure?: boolean): Promise<Post> {
    return _getPost(id, pure ?? false);
  }

  return {
    getPost,
    getPreviewPost: _getPreviewPost,
    createPost: _createPost,
    deletePost: _deletePost,
  };
}
