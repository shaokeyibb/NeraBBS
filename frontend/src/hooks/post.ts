import useBackend from "./backend.ts";

export default function usePost() {
  const { _getPost, _getPreviewPost } = useBackend();

  const getPost = async (id: number) => await _getPost(id);

  return {
    getPost,
    getPreviewPost: _getPreviewPost,
  };
}
