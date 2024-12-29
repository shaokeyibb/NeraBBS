export async function isSupported(): Promise<boolean> {
    // Availability of `window.PublicKeyCredential` means WebAuthn is usable.
    // `isUserVerifyingPlatformAuthenticatorAvailable` means the feature detection is usable.
    // `isConditionalMediationAvailable` means the feature detection is usable.
    if (
        window.PublicKeyCredential &&
        PublicKeyCredential.isUserVerifyingPlatformAuthenticatorAvailable &&
        PublicKeyCredential.isConditionalMediationAvailable
    ) {
        // Check if user verifying platform authenticator is available.
        const results = await Promise.all([
            PublicKeyCredential.isUserVerifyingPlatformAuthenticatorAvailable(),
            PublicKeyCredential.isConditionalMediationAvailable(),
        ]);
        if (results.every((r) => r)) {
            return true;
        }
    }
    return false;
}
