import {useMemoize} from "@vueuse/core";
import useBackend from "./backend.ts";
import {useSessionStore} from "../stores/session.ts";
import type {PatchUserProfileReq} from "../types/backend.ts";

export default function useUser() {
  const { _getUserInfo, _getUserProfile, _patchUserProfile, _changePassword } =
    useBackend();
  const sessionStore = useSessionStore();

  const _getUserProfileMemorized = useMemoize(_getUserProfile, {
    getKey: (id) => id ?? -1,
  });

  const getUserProfile = async (id?: number) => {
    if (id === undefined) {
      try {
        const res = await _getUserProfile();
        sessionStore.userProfile = res;
        return res;
      } catch {
        sessionStore.clearSession();
        return undefined;
      }
    }
    return await _getUserProfileMemorized(id);
  };

  const patchUserProfile = async (data: PatchUserProfileReq) => {
    const res = await _patchUserProfile(data);
    sessionStore.userProfile = res;
    return res;
  };

  const _getUserInfoMemorized = useMemoize(_getUserInfo, {
    getKey: (id) => id ?? -1,
  });

  const getUserInfo = async (id?: number) => {
    if (id === undefined) {
      try {
        const res = await _getUserInfo();
        sessionStore.userInfo = res;
        return res;
      } catch {
        sessionStore.clearSession();
        return undefined;
      }
    }
    return await _getUserInfoMemorized(id);
  };

  const refreshUserSession = async () => {
    getUserProfile();
    await getUserInfo();
  };

  const changePassword = async (newPassword: string) => {
    await _changePassword(newPassword);
  };

  return {
    getUserInfo,
    getUserProfile,
    patchUserProfile,
    refreshUserSession,
    changePassword,
  };
}
