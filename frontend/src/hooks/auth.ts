import useBackend from "./backend.ts";
import { useSessionStore } from "../stores/session.ts";
import useUser from "./user.ts";
import { computed } from "vue";

export default function useAuth() {
  const { _signIn, _signUp, _signOut } = useBackend();
  const sessionStore = useSessionStore();
  const { refreshUserSession } = useUser();

  const signIn = async (email: string, password: string) => {
    await _signIn(email, password);
    await refreshUserSession();
  };

  const signUp = async (email: string, password: string) => {
    await _signUp(email, password);
    await refreshUserSession();
  };

  const signOut = async () => {
    await _signOut();
    sessionStore.clearSession();
  };

  const isLoggedIn = computed(() => sessionStore.userInfo !== undefined);

  return {
    signIn,
    signUp,
    signOut,
    isLoggedIn,
  };
}
