import useBackend from "./backend.ts";

export default function usePost() {
  const { _getPost, _getPreviewPost, _createPost, _deletePost } = useBackend();

  return {
    getPost: _getPost,
    getPreviewPost: _getPreviewPost,
    createPost: _createPost,
    deletePost: _deletePost,
  };
}
