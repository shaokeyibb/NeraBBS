package io.hikarilan.nerabbs.services.webauthn.handler;

import com.yubico.webauthn.exception.AssertionFailedException;
import io.grpc.Status;
import io.hikarilan.nerabbs.common.exception.UnauthorizedException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class GrpcExceptionAdvice {

    @GrpcExceptionHandler
    public Status handleNotLoginException(UnauthorizedException e) {
        return Status.UNAUTHENTICATED.withCause(e);
    }

    @GrpcExceptionHandler
    public Status handleAssertionFailed(AssertionFailedException e) {
        return Status.UNAUTHENTICATED.withCause(e);
    }
}
