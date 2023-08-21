package io.hikarilan.nerabbs.services.webauthn.service;

import cn.hutool.core.util.ByteUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.*;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import io.hikarilan.nerabbs.lib.services.user.grpc.BasicUserInfoRequest;
import io.hikarilan.nerabbs.lib.services.user.grpc.FullUserInfoRequest;
import io.hikarilan.nerabbs.lib.services.user.grpc.UserInfoGrpc;
import io.hikarilan.nerabbs.services.webauthn.database.entity.WebauthnCredentialEntity;
import io.hikarilan.nerabbs.services.webauthn.database.repository.WebauthnCredentialRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PasskeyService {

    private final WebauthnCredentialRepository webauthnCredentialRepository;
    private final RelyingParty relyingParty;
    private final StringRedisTemplate template;
    private final String REDIS_PASSKEY_REGISTRATION_KEY = "passkey:registration";
    private final String REDIS_PASSKEY_ASSERTION_KEY = "passkey:assertion";
    @GrpcClient("nerabbs-service-user")
    private UserInfoGrpc.UserInfoBlockingStub userInfoStub;

    public PublicKeyCredentialCreationOptions startPasskeyRegistration(long userID) throws JsonProcessingException {
        var user = userInfoStub.getBasicUserInfo(BasicUserInfoRequest.newBuilder().setId(userID).build());

        var options = relyingParty.startRegistration(StartRegistrationOptions.builder()
                .user(UserIdentity.builder()
                        .name(user.getEmail())
                        .displayName(user.getUsername())
                        .id(new ByteArray(ByteUtil.longToBytes(user.getId())))
                        .build())
                .authenticatorSelection(AuthenticatorSelectionCriteria.builder()
                        .residentKey(ResidentKeyRequirement.REQUIRED)
                        .build())
                .build());

        template.opsForHash().put(REDIS_PASSKEY_REGISTRATION_KEY, String.valueOf(user.getId()), options.toJson());

        return options;
    }

    public void finishPasskeyRegistration(long userID, PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> pkc) throws JsonProcessingException, RegistrationFailedException {
        var user = userInfoStub.getBasicUserInfo(BasicUserInfoRequest.newBuilder().setId(userID).build());

        var request = PublicKeyCredentialCreationOptions.fromJson((String) template.opsForHash().get(REDIS_PASSKEY_REGISTRATION_KEY, String.valueOf(user.getId())));

        var result = relyingParty.finishRegistration(FinishRegistrationOptions.builder()
                .request(request)
                .response(pkc)
                .build());

        template.opsForHash().delete(REDIS_PASSKEY_REGISTRATION_KEY, String.valueOf(user.getId()));

        storeCredential(user.getId(), request, result);
    }

    public AssertionRequest startPasskeyAssertion(String identifier) throws JsonProcessingException {
        var options = relyingParty.startAssertion(StartAssertionOptions.builder().build());

        template.opsForHash().put(REDIS_PASSKEY_ASSERTION_KEY, identifier, options.toJson());

        return options;
    }

    public long finishPasskeyAssertion(String identifier, PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> pkc) throws JsonProcessingException, AssertionFailedException {
        var request = AssertionRequest.fromJson((String) template.opsForHash().get(REDIS_PASSKEY_ASSERTION_KEY, identifier));

        var result = relyingParty.finishAssertion(FinishAssertionOptions.builder()
                .request(request)
                .response(pkc)
                .build());

        template.opsForHash().delete(REDIS_PASSKEY_ASSERTION_KEY, identifier);

        if (!result.isSuccess()) {
            throw new AssertionFailedException("Verify failed");
        }

        var user = userInfoStub.getFullUserInfo(FullUserInfoRequest.newBuilder().setEmail(result.getUsername()).build());

        updateCredential(user.getId(), result.getCredential().getCredentialId(), result);

        return user.getId();
    }

    private void storeCredential(long id,
                                 @NotNull PublicKeyCredentialCreationOptions request,
                                 @NotNull RegistrationResult result) {
        webauthnCredentialRepository.save(WebauthnCredentialEntity.fromFinishPasskeyRegistration(id, request, result));
    }

    private void updateCredential(long id,
                                  @NotNull ByteArray credentialId,
                                  @NotNull AssertionResult result) {
        var entity = webauthnCredentialRepository.findAllByUserID(id).stream()
                .filter(it -> credentialId.equals(it.getCredentialRegistration().getCredential().getCredentialId()))
                .findAny()
                .orElseThrow();

        entity.getCredentialRegistration().setCredential(entity.getCredentialRegistration().getCredential().toBuilder().signatureCount(result.getSignatureCount()).build());

        webauthnCredentialRepository.saveAndFlush(entity);
    }


}
