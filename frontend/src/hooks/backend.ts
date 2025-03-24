import { apiBaseUrl } from "../config.ts";
import type {
  Comment,
  CommentChain,
  CreateCommentReq,
  Hit,
  Passkey,
  PatchUserProfileReq,
  Post,
  PreviewPost,
  SearchResp,
  UserInfo,
  UserProfile,
} from "../types/backend.ts";
import type { ErrorMessage } from "../types/error-handling.ts";
import type {
  AuthenticationPublicKeyCredential,
  CredentialCreationOptionsJSON,
  CredentialRequestOptionsJSON,
  RegistrationPublicKeyCredential,
} from "@github/webauthn-json/browser-ponyfill";

const $fetch = async <T = unknown>(
  options:
    | {
        path: string;
        method?: "GET";
        data?: Record<string, string | number | undefined>;
        requestOptions?: RequestInit;
      }
    | {
        path: string;
        method: "POST" | "PUT" | "PATCH" | "DELETE";
        data?: object;
        requestOptions?: RequestInit;
      }
    | string,
) => {
  const {
    path,
    method = "GET",
    data = undefined,
    requestOptions = {},
  } = typeof options === "string" ? { path: options } : options;

  const url = new URL(`${window.location.origin}${apiBaseUrl}/${path}`);

  if (method === "GET" && data !== undefined) {
    const _data = data as Record<string, string | number | undefined>;
    for (const k in _data) {
      const v = _data[k];
      if (v !== undefined) url.searchParams.append(k, v.toString());
    }
  }

  let res: Response;

  try {
    const body =
      method === "GET" || data === undefined
        ? undefined
        : data instanceof FormData
          ? data
          : JSON.stringify(data);

    const headers = new Headers();
    if (method !== "GET" && !(data instanceof FormData)) {
      headers.set("Content-Type", "application/json");
    }

    res = await fetch(url.toString(), {
      method,
      headers,
      body,
      ...requestOptions,
    });
  } catch {
    throw {
      code: -1,
      message: "Network error",
    } as ErrorMessage;
  }

  const _resp = await res.text();
  const resp = /^(\{.*})|(\[.*])$/.test(_resp) ? JSON.parse(_resp) : _resp;

  if (!res.ok) {
    const error = resp as ErrorMessage;

    throw {
      code: res.status ?? -1,
      message: error.message ?? "Internal error",
    } as ErrorMessage;
  }

  return resp as T;
};

export default function useBackend() {
  const _getPost = async (id: number, pure: boolean) => {
    return await $fetch<Post>(`posts/${id}?pure=${pure}`);
  };

  const _getPreviewPost = async (page: number, size: number) => {
    return await $fetch<PreviewPost[]>({
      path: `posts`,
      data: {
        page,
        size,
      },
    });
  };

  const _createPost = async (data: { title?: string; content: string }) => {
    return await $fetch<number>({
      path: `posts`,
      method: "POST",
      data,
    });
  };

  const _deletePost = async (id: number) => {
    return await $fetch({
      path: `posts/${id}`,
      method: "DELETE",
    });
  };

  const _getUserInfo = async (id?: number) => {
    return await $fetch<UserInfo>(`users${id === undefined ? "" : `/${id}`}`);
  };

  const _getUserProfile = async (id?: number) => {
    return await $fetch<UserProfile>(
      `users/profile${id === undefined ? "" : `/${id}`}`,
    );
  };

  const _patchUserProfile = async (data: PatchUserProfileReq) => {
    const formData = new FormData();
    for (const [key, value] of Object.entries(data)) {
      formData.append(key, value);
    }
    return await $fetch<UserProfile>({
      path: `users/profile`,
      method: "PATCH",
      data: formData,
    });
  };

  const _signIn = async (email: string, password: string) => {
    await $fetch({
      path: "authorization/signin",
      method: "POST",
      data: { email, password },
    });
  };

  const _signUp = async (email: string, password: string) => {
    await $fetch({
      path: "authorization/signup",
      method: "POST",
      data: { email, password },
    });
  };

  const _signOut = async () => {
    await $fetch({ path: "authorization/signout", method: "POST" });
  };

  const _getPasskeyRegistrationOptions = async () => {
    return await $fetch<CredentialCreationOptionsJSON>(
      "authorization/passkey/registration/options",
    );
  };

  const _verifyPasskeyRegistration = async (
    registrationPublicKeyCredential: RegistrationPublicKeyCredential,
  ) => {
    return await $fetch({
      path: "authorization/passkey/registration",
      method: "POST",
      data: registrationPublicKeyCredential,
    });
  };

  const _getPasskeyAssertionOptions = async () => {
    return await $fetch<CredentialRequestOptionsJSON>(
      "authorization/passkey/assertion/options",
    );
  };

  const _verifyPasskeyAssertion = async (
    authenticationPublicKeyCredential: AuthenticationPublicKeyCredential,
  ) => {
    return await $fetch({
      path: "authorization/passkey/assertion",
      method: "POST",
      data: authenticationPublicKeyCredential,
    });
  };

  const _getPasskeys = async () => {
    return await $fetch<Passkey[]>("webauthn/passkey");
  };

  const _removePasskey = async (id: number) => {
    return await $fetch({
      path: `webauthn/passkey/${id}`,
      method: "DELETE",
    });
  };

  const _changePassword = async (newPassword: string) => {
    await $fetch({
      path: `users/password`,
      method: "PUT",
      data: { newPassword },
    });
  };

  const _search = async <T>(
    index: string,
    query: string,
    offset: number,
    limit: number,
  ) => {
    return await $fetch<SearchResp<T>>({
      path: `search/${index}/${query}?offset=${offset}&limit=${limit}`,
    });
  };

  const _getHitCount = async (topic: string, key: string) => {
    return await $fetch<Hit>(`hit/${topic}/${key}`);
  };

  const _getTrending = async (topic: string, limit: number) => {
    return await $fetch<Hit[]>(`trending/${topic}?limit=${limit}`);
  };

  const _getComment = async (postID: number, commentID: number) => {
    return await $fetch<Comment>(`posts/${postID}/comments/${commentID}`);
  };

  const _getComments = async (postID: number) => {
    return await $fetch<CommentChain[]>(`posts/${postID}/comments`);
  };

  const _createComment = async (postID: number, data: CreateCommentReq) => {
    return await $fetch<number>({
      path: `posts/${postID}/comments`,
      method: "POST",
      data,
    });
  };

  const _deleteComment = async (postID: number, commentID: number) => {
    await $fetch({
      path: `posts/${postID}/comments/${commentID}`,
      method: "DELETE",
    });
  };

  return {
    _getPost,
    _getPreviewPost,
    _createPost,
    _deletePost,
    _getUserInfo,
    _getUserProfile,
    _patchUserProfile,
    _signIn,
    _signUp,
    _signOut,
    _getPasskeyRegistrationOptions,
    _verifyPasskeyRegistration,
    _getPasskeyAssertionOptions,
    _verifyPasskeyAssertion,
    _getPasskeys,
    _removePasskey,
    _changePassword,
    _search,
    _getHitCount,
    _getTrending,
    _getComment,
    _getComments,
    _createComment,
    _deleteComment,
  };
}
