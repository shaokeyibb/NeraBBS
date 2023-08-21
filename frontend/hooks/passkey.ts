import {
    create,
    CredentialCreationOptionsJSON,
    CredentialRequestOptionsJSON,
    get,
    parseCreationOptionsFromJSON,
    parseRequestOptionsFromJSON
} from "@github/webauthn-json/browser-ponyfill";

export default function usePasskey() {

    async function isSupported(): Promise<boolean> {
        // Availability of `window.PublicKeyCredential` means WebAuthn is usable.
        // `isUserVerifyingPlatformAuthenticatorAvailable` means the feature detection is usable.
        // `isConditionalMediationAvailable` means the feature detection is usable.
        if (window.PublicKeyCredential &&
            PublicKeyCredential.isUserVerifyingPlatformAuthenticatorAvailable &&
            PublicKeyCredential.isConditionalMediationAvailable
        ) {
            // Check if user verifying platform authenticator is available.
            const results = await Promise.all([
                PublicKeyCredential.isUserVerifyingPlatformAuthenticatorAvailable(),
                PublicKeyCredential.isConditionalMediationAvailable(),
            ])
            if (results.every(r => r === true)) {
                return true;
            }
        }
        return false
    }

    async function createPasskeyCredential() {
        const json = await $fetch<CredentialCreationOptionsJSON>("/api/authorization/passkey/registration/options", {
            method: "GET",
            parseResponse: JSON.parse
        })
        const options = parseCreationOptionsFromJSON(json)

        const response = await create(options);

        await $fetch("/api/authorization/passkey/registration", {
            method: "POST",
            body: JSON.stringify(response),
        })
    }

    async function validatePasskeyCredential(): Promise<number> {
        const json = await $fetch<CredentialRequestOptionsJSON>("/api/authorization/passkey/assertion/options", {
            method: "GET",
            parseResponse: JSON.parse
        })
        const options = parseRequestOptionsFromJSON(json)
        const response = await get(options);

        return await $fetch("/api/authorization/passkey/assertion", {
            method: "POST",
            body: JSON.stringify(response),
            parseResponse: (it) => +it
        })
    }

    return {
        isSupported,
        createPasskeyCredential,
        validatePasskeyCredential
    }

}