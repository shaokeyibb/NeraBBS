package io.hikarilan.nerabbs.services.auth.service;

import io.hikarilan.nerabbs.lib.services.webauthn.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class PasskeyAuthorizationService {

    @GrpcClient("nerabbs-service-webauthn")
    private PasskeyGrpc.PasskeyBlockingStub passkeyService;

    public String getPasskeyRegistrationOptions(long id) {
        return passkeyService.getPasskeyRegistrationOptions(GetPasskeyRegistrationOptionsRequest.newBuilder()
                        .setId(id)
                        .build())
                .getOptions();
    }

    public void verifyPasskeyRegistration(long id, String credential) {
        passkeyService.verifyPasskeyRegistration(VerifyPasskeyRegistrationRequest.newBuilder()
                .setId(id)
                .setCredential(credential)
                .build());
    }

    public String getPasskeyAssertionOptions(String identifier) {
        return passkeyService.getPasskeyAssertionOptions(GetPasskeyAssertionOptionsRequest.newBuilder()
                        .setIdentifier(identifier)
                        .build())
                .getOptions();
    }

    public long verifyPasskeyAssertion(String identifier, String credential) {
        return passkeyService.verifyPasskeyAssertion(VerifyPasskeyAssertionRequest.newBuilder()
                        .setIdentifier(identifier)
                        .setCredential(credential)
                        .build())
                .getId();
    }

}
