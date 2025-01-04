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
    _getPasskeys,
    _removePasskey,
  } = useBackend();

  const { refreshUserSession } = useUser();

  const getCreationOptions = async () =>
    parseCreationOptionsFromJSON(await _getPasskeyRegistrationOptions());

  const getRequestOptions = async () =>
    parseRequestOptionsFromJSON(await _getPasskeyAssertionOptions());

  const createPasskeyCredential = async () => {
    const options = await getCreationOptions();
    const resp = await create(options);
    await _verifyPasskeyRegistration(resp);
  };

  const validatePasskeyCredential = async (
    conditional?: boolean,
    hooks?: {
      beforeVerifyAssertion?: () => void;
    },
  ) => {
    const options = await getRequestOptions();

    if (conditional) {
      options.mediation = "conditional";
    }

    const resp = await get(options);
    if (hooks?.beforeVerifyAssertion) {
      hooks.beforeVerifyAssertion();
    }
    await _verifyPasskeyAssertion(resp);
    await refreshUserSession();
  };

  const getPasskeys = async () => await _getPasskeys();

  const removePasskey = async (id: number) => await _removePasskey(id);

  return {
    createPasskeyCredential,
    validatePasskeyCredential,
    getPasskeys,
    removePasskey,
  };
}
