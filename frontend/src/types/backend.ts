export type PostCreation = Partial<Pick<Post, "title">> & Pick<Post, "content">;

export type Post = {
  id: number;
  posterID: number;
  title?: string | undefined;
  content: string;
  createAt: string;
};

export type PreviewPost = Readonly<Post>;

export type UserInfo = {
  id: number;
  email: string;
  createAt: string;
};

export type UserProfile = {
  userID: number;
  username?: string;
  avatarPath?: string;
  signature?: string;
};

export type PutUserProfileReq = Omit<UserProfile, "avatarPath" | "userID"> & {
  avatar: File | null;
};

export type PatchUserProfileReq = Partial<
  Omit<PutUserProfileReq, "avatar"> & {
    avatar: File;
  }
>;

export type Passkey = {
  id: number;
  userId: number;
  nickname: string;
  signatureCount: number;
  createAt: string;
};

export type SearchResp<T> = ({
  [key in keyof T]: T[key];
} & {
  _matchesPosition: {
    [key in keyof T]: {
      start: number;
      length: number;
    }[];
  };
})[];

export type Hit = {
  topic: string;
  key: string;
  hitCount: number;
};

export type Comment = {
  id: number;
  postID: number;
  commenterID: number;
  rootCommentID: number;
  parentCommentID?: number;
  content: string;
  createAt: string;
  isDeleted: boolean;
};

export type CommentChain = {
  comment: Comment;
  children: CommentChain[];
};

export type CreateCommentReq = {
  parentCommentID?: number;
  content: string;
};
