import useBackend from "./backend.ts";

export default function useAuth() {
  const { _signIn, _signUp, _signOut } = useBackend();

  const signIn = async (email: string, password: string) => {
    await _signIn(email, password);
  };

  const signUp = async (email: string, password: string) => {
    await _signUp(email, password);
  };

  const signOut = async () => {
    await _signOut();
  };

  return {
    signIn,
    signUp,
    signOut,
  };
}
