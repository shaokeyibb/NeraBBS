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

export type UserProfilePutReq = Omit<UserProfile, "avatarPath"> & {
  avatar: File | null;
};

export type UserProfilePatchReq = Partial<UserProfilePutReq>;
