package io.hikarilan.nerabbs.services.webauthn.webauthn;

import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import io.hikarilan.nerabbs.lib.services.user.grpc.BasicUserInfoRequest;
import io.hikarilan.nerabbs.lib.services.user.grpc.UserInfoGrpc;
import io.hikarilan.nerabbs.services.webauthn.data.CredentialRegistration;
import io.hikarilan.nerabbs.services.webauthn.database.entity.WebauthnCredentialEntity;
import io.hikarilan.nerabbs.services.webauthn.database.repository.WebauthnCredentialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class CredentialRepositoryImpl implements CredentialRepository {


    private final WebauthnCredentialRepository webauthnCredentialRepository;

    @GrpcClient("nerabbs-service-user")
    private UserInfoGrpc.UserInfoBlockingStub userInfoStub;

    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String email) {
        return webauthnCredentialRepository.findAllByUserID(getUserIDByEmail(email)).stream()
                .map(WebauthnCredentialEntity::getCredentialRegistration)
                .map(it -> PublicKeyCredentialDescriptor.builder()
                        .id(it.getCredential().getCredentialId())
                        .transports(it.getTransports())
                        .build())
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        return getRegistrationsByUserHandle(userHandle).stream()
                .findAny()
                .map(CredentialRegistration::getUsername);
    }

    @Override
    public Optional<ByteArray> getUserHandleForUsername(String email) {
        return getRegistrationsByUsername(email).stream()
                .findAny()
                .map(reg -> reg.getUserIdentity().getId());
    }

    @Override
    public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
//        Optional<CredentialRegistration> registrationMaybe = webauthnCredentialRepository.findAllByCredentialRegistrationCredentialCredentialId(credentialId).stream()
//                .map(WebauthnCredentialEntity::getCredentialRegistration)
//                .findAny();
        Optional<CredentialRegistration> registrationMaybe = webauthnCredentialRepository.findAll().stream()
                .map(WebauthnCredentialEntity::getCredentialRegistration)
                .filter(it -> it.getCredential().getCredentialId().equals(credentialId))
                .findAny();

        log.debug(
                "lookup credential ID: {}, user handle: {}; result: {}",
                credentialId,
                userHandle,
                registrationMaybe);

        return registrationMaybe.map(it ->
                RegisteredCredential.builder()
                        .credentialId(it.getCredential().getCredentialId())
                        .userHandle(it.getCredential().getUserHandle())
                        .publicKeyCose(it.getCredential().getPublicKeyCose())
                        .signatureCount(it.getCredential().getSignatureCount())
                        .build());
    }

    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
//        return webauthnCredentialRepository.findAllByCredentialRegistrationCredentialCredentialId(credentialId).stream()
//                .map(WebauthnCredentialEntity::getCredentialRegistration)
//                .map(it ->
//                        RegisteredCredential.builder()
//                                .credentialId(it.getCredential().getCredentialId())
//                                .userHandle(it.getCredential().getUserHandle())
//                                .publicKeyCose(it.getCredential().getPublicKeyCose())
//                                .signatureCount(it.getCredential().getSignatureCount())
//                                .build())
//                .collect(Collectors.toUnmodifiableSet());
        return webauthnCredentialRepository.findAll().stream()
                .map(WebauthnCredentialEntity::getCredentialRegistration)
                .filter(it -> it.getCredential().getCredentialId().equals(credentialId))
                .map(it ->
                        RegisteredCredential.builder()
                                .credentialId(it.getCredential().getCredentialId())
                                .userHandle(it.getCredential().getUserHandle())
                                .publicKeyCose(it.getCredential().getPublicKeyCose())
                                .signatureCount(it.getCredential().getSignatureCount())
                                .build())
                .collect(Collectors.toUnmodifiableSet());
    }

    private long getUserIDByEmail(String email) {
        return userInfoStub.getUserInfo(BasicUserInfoRequest.newBuilder().setEmail(email).build()).getId();
    }

    private Collection<CredentialRegistration> getRegistrationsByUsername(String email) {
        return webauthnCredentialRepository.findAllByUserID(getUserIDByEmail(email)).stream()
                .map(WebauthnCredentialEntity::getCredentialRegistration)
                .toList();
    }

    private Collection<CredentialRegistration> getRegistrationsByUserHandle(ByteArray userHandle) {
//        return webauthnCredentialRepository.findAllByCredentialRegistrationUserIdentityId(userHandle).stream()
//                .map(WebauthnCredentialEntity::getCredentialRegistration)
//                .toList();
        return webauthnCredentialRepository.findAll().stream()
                .map(WebauthnCredentialEntity::getCredentialRegistration)
                .filter(it -> it.getUserIdentity().getId().equals(userHandle))
                .toList();
    }
}
