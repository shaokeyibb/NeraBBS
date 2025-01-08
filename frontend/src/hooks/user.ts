import { useMemoize } from "@vueuse/core";
import useBackend from "./backend.ts";
import { useSessionStore } from "../stores/session.ts";

export default function useUser() {
  const { _getUserInfo, _getUserProfile } = useBackend();
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

  const refreshUserSession = async () => await getUserInfo();

  return {
    getUserInfo,
    getUserProfile,
    refreshUserSession,
  };
}
