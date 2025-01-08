import { defineStore } from "pinia";
import type { UserInfo, UserProfile } from "../types/backend.ts";
import { ref } from "vue";

export const useSessionStore = defineStore(
  "session",
  () => {
    const userInfo = ref<UserInfo>();
    const userProfile = ref<UserProfile>();

    function clearSession() {
      userInfo.value = undefined;
      userProfile.value = undefined;
    }

    return {
      userInfo,
      userProfile,
      clearSession,
    };
  },
  {
    persist: {
      storage: sessionStorage,
    },
  },
);
