import useBackend from "./backend.ts";

export default function useComment() {
  const { _getComment, _getComments, _createComment, _deleteComment } =
    useBackend();

  return {
    getComment: _getComment,
    getComments: _getComments,
    createComment: _createComment,
    deleteComment: _deleteComment,
  };
}
