import useBackend from "./backend.ts";
import type { ErrorMessage } from "../types/error-handling.ts";

export default function useAuth() {
  const { _signIn, _signUp, _signOut, _getUserInfo } = useBackend();

  const signIn = async (email: string, password: string) => {
    await _signIn(email, password);
  };

  const signUp = async (email: string, password: string) => {
    await _signUp(email, password);
  };

  const signOut = async () => {
    await _signOut();
  };

  const isLoggedIn = async () => {
    try {
      await _getUserInfo();
      return true;
    } catch (e) {
      const err = e as ErrorMessage;
      if (err.code == 401) {
        return false;
      }
      throw e;
    }
  };

  return {
    signIn,
    signUp,
    signOut,
    isLoggedIn,
  };
}
