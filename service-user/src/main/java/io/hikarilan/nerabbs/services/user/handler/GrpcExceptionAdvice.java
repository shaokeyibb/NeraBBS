package io.hikarilan.nerabbs.services.user.handler;

import io.grpc.Status;
import io.hikarilan.nerabbs.common.exception.UnauthorizedException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class GrpcExceptionAdvice {

    @GrpcExceptionHandler
    public Status handleException(IllegalArgumentException e) {
        return Status.INVALID_ARGUMENT.withCause(e);
    }

    @GrpcExceptionHandler
    public Status handleNotLoginException(UnauthorizedException e) {
        return Status.UNAUTHENTICATED.withCause(e);
    }

}
