import {createFetch} from "@vueuse/core";
import {apiBaseUrl} from "../config.ts";
import type {Post, PreviewPost, UserInfo, UserProfile,} from "../types/backend.ts";
import type {ErrorMessage} from "../types/error-handling.ts";
import type {
    AuthenticationPublicKeyCredential,
    CredentialCreationOptionsJSON,
    CredentialRequestOptionsJSON,
    RegistrationPublicKeyCredential,
} from "@github/webauthn-json/browser-ponyfill";

const $fetch = createFetch({
  baseUrl: apiBaseUrl,
  options: {
    onFetchError: (ctx) => {
      ctx.error = {
        code: ctx.response?.status ?? ctx.error.code ?? -1,
        message: ctx.data?.message ?? ctx.error.message ?? "Internal error",
      } as ErrorMessage;

      return ctx;
    },
  },
});

export default function useBackend() {
  const _getPost = async (id: number) => {
    const res = await $fetch(`posts/${id}`).get().json<Post>();
    if (res.error.value) {
      throw res.error.value as ErrorMessage;
    }
    return res.data.value!;
  };

  const _getPreviewPost = async (page: number, size: number) => {
    const res = await $fetch(`posts?page=${page}&size=${size}`)
      .get()
      .json<PreviewPost[]>();
    if (res.error.value) {
      throw res.error.value as ErrorMessage;
    }
    return res.data.value!;
  };

  const _getUserInfo = async (id?: number) => {
    const res = await $fetch(`users${id === undefined ? "" : `/${id}`}`)
      .get()
      .json<UserInfo>();
    if (res.error.value) {
      throw res.error.value as ErrorMessage;
    }
    return res.data.value!;
  };

  const _getUserProfile = async (id?: number) => {
    const res = await $fetch(`users/profile${id === undefined ? "" : `/${id}`}`)
      .get()
      .json<UserProfile>();
    if (res.error.value) {
      throw res.error.value as ErrorMessage;
    }
    return res.data.value!;
  };

  const _signIn = async (email: string, password: string) => {
    const res = await $fetch("authorization/signin").post({ email, password });
    if (res.error.value) {
      throw res.error.value as ErrorMessage;
    }
  };

  const _signUp = async (email: string, password: string) => {
    const res = await $fetch("authorization/signup").post({ email, password });
    if (res.error.value) {
      throw res.error.value as ErrorMessage;
    }
  };

  const _signOut = async () => {
    const res = await $fetch("authorization/signout").post();
    if (res.error.value) {
      throw res.error.value as ErrorMessage;
    }
  };

  const _getPasskeyRegistrationOptions = async () => {
    const res = await $fetch("authorization/passkey/registration/options")
      .get()
      .json<CredentialCreationOptionsJSON>();
    if (res.error.value) {
      throw res.error.value as ErrorMessage;
    }
    return res.data.value!;
  };

  const _verifyPasskeyRegistration = async (
    registrationPublicKeyCredential: RegistrationPublicKeyCredential,
  ) => {
    const res = await $fetch("authorization/passkey/registration")
      .post(registrationPublicKeyCredential, "json")
      .json();
    if (res.error.value) {
      throw res.error.value as ErrorMessage;
    }
  };

  const _getPasskeyAssertionOptions = async () => {
    const res = await $fetch("authorization/passkey/assertion/options")
      .get()
      .json<CredentialRequestOptionsJSON>();
    if (res.error.value) {
      throw res.error.value as ErrorMessage;
    }
    return res.data.value!;
  };

  const _verifyPasskeyAssertion = async (
    authenticationPublicKeyCredential: AuthenticationPublicKeyCredential,
  ) => {
    const res = await $fetch("authorization/passkey/assertion")
      .post(authenticationPublicKeyCredential, "json")
      .json();
    if (res.error.value) {
      throw res.error.value as ErrorMessage;
    }
  };

  return {
    _getPost,
    _getPreviewPost,
    _getUserInfo,
    _getUserProfile,
    _signIn,
    _signUp,
    _signOut,
    _getPasskeyRegistrationOptions,
    _verifyPasskeyRegistration,
    _getPasskeyAssertionOptions,
    _verifyPasskeyAssertion,
  };
}
