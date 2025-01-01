import { computedAsync, useMemoize } from "@vueuse/core";
import useBackend from "./backend.ts";

export default function useUser() {
  const { _getUserInfo, _getUserProfile } = useBackend();

  const _getUserProfileMemorized = useMemoize(_getUserProfile, {
    getKey: (id) => id ?? -1,
  });

  const getUserProfile = (id?: number) => {
    return computedAsync(() => _getUserProfileMemorized(id));
  };

  const _getUserInfoMemorized = useMemoize(_getUserInfo, {
    getKey: (id) => id ?? -1,
  });

  const getUserInfo = (id?: number) => {
    return computedAsync(() => _getUserInfoMemorized(id));
  };

  return {
    getUserInfo,
    getUserProfile,
  };
}
