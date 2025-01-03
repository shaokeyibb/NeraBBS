import useBackend from "./backend.ts";
import {
    create,
    get,
    parseCreationOptionsFromJSON,
    parseRequestOptionsFromJSON,
} from "@github/webauthn-json/browser-ponyfill";
import useUser from "./user.ts";

export default function usePasskey() {
  const {
    _getPasskeyRegistrationOptions,
    _verifyPasskeyRegistration,
    _getPasskeyAssertionOptions,
    _verifyPasskeyAssertion,
  } = useBackend();

  const { refreshUserSession } = useUser();

  const createPasskeyCredential = async () => {
    const options = parseCreationOptionsFromJSON(
      await _getPasskeyRegistrationOptions(),
    );
    const resp = await create(options);
    await _verifyPasskeyRegistration(resp);
  };

  const validatePasskeyCredential = async () => {
    const options = parseRequestOptionsFromJSON(
      await _getPasskeyAssertionOptions(),
    );
    const resp = await get(options);
    await _verifyPasskeyAssertion(resp);
    await refreshUserSession();
  };

  return {
    createPasskeyCredential,
    validatePasskeyCredential,
  };
}
