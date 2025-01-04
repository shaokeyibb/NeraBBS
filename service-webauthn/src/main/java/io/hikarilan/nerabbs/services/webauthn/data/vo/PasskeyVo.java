package io.hikarilan.nerabbs.services.webauthn.data.vo;

import io.hikarilan.nerabbs.services.webauthn.database.entity.WebauthnCredentialEntity;
import jakarta.validation.constraints.NotNull;

public record PasskeyVo(
        long id,
        long userId,
        String nickname,
        String createAt
) {

    @NotNull
    public static PasskeyVo fromWebauthnCredentialEntity(@NotNull WebauthnCredentialEntity entity) {
        return new PasskeyVo(entity.getId(),
                entity.getUserID(),
                entity.getCredentialRegistration().getCredentialNickname(),
                String.valueOf(entity.getCredentialRegistration().getRegistration()));
    }

}
