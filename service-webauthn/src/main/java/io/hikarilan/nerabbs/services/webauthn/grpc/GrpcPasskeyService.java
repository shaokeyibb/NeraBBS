package io.hikarilan.nerabbs.services.webauthn.grpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.protobuf.Empty;
import com.yubico.webauthn.data.PublicKeyCredential;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import io.grpc.stub.StreamObserver;
import io.hikarilan.nerabbs.lib.services.webauthn.grpc.*;
import io.hikarilan.nerabbs.services.webauthn.service.PasskeyService;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.io.IOException;

@RequiredArgsConstructor
@GrpcService
public class GrpcPasskeyService extends PasskeyGrpc.PasskeyImplBase {

    private final PasskeyService passkeyService;

    @Override
    public void getPasskeyRegistrationOptions(GetPasskeyRegistrationOptionsRequest request, StreamObserver<GetPasskeyRegistrationOptionsResponse> responseObserver) {
        try {
            var resp = GetPasskeyRegistrationOptionsResponse.newBuilder()
                    .setOptions(passkeyService.startPasskeyRegistration(request.getId()).toCredentialsCreateJson())
                    .build();
            responseObserver.onNext(resp);
            responseObserver.onCompleted();
        } catch (JsonProcessingException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void verifyPasskeyRegistration(VerifyPasskeyRegistrationRequest request, StreamObserver<Empty> responseObserver) {
        try {
            passkeyService.finishPasskeyRegistration(request.getId(), PublicKeyCredential.parseRegistrationResponseJson(request.getCredential()));
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (RegistrationFailedException | IOException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getPasskeyAssertionOptions(GetPasskeyAssertionOptionsRequest request, StreamObserver<GetPasskeyAssertionOptionsResponse> responseObserver) {
        try {
            var resp = GetPasskeyAssertionOptionsResponse.newBuilder()
                    .setOptions(passkeyService.startPasskeyAssertion(request.getIdentifier()).toCredentialsGetJson())
                    .build();
            responseObserver.onNext(resp);
            responseObserver.onCompleted();
        } catch (JsonProcessingException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void verifyPasskeyAssertion(VerifyPasskeyAssertionRequest request, StreamObserver<VerifyPasskeyAssertionResponse> responseObserver) {
        try {
            var resp = VerifyPasskeyAssertionResponse.newBuilder()
                    .setId(passkeyService.finishPasskeyAssertion(request.getIdentifier(), PublicKeyCredential.parseAssertionResponseJson(request.getCredential())))
                    .build();
            responseObserver.onNext(resp);
            responseObserver.onCompleted();
        } catch (IOException | AssertionFailedException e) {
            responseObserver.onError(e);
        }
    }
}
