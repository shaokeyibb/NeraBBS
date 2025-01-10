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
