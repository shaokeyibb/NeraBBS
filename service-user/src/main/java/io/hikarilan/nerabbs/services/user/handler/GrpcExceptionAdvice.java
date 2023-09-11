package io.hikarilan.nerabbs.services.user.handler;

import io.grpc.Status;
import io.hikarilan.nerabbs.common.exception.UnauthorizedException;
import io.hikarilan.nerabbs.common.exception.UserAlreadyExistsException;
import io.hikarilan.nerabbs.common.exception.UserNotFoundException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class GrpcExceptionAdvice {

    @GrpcExceptionHandler
    public Status handleException(UserAlreadyExistsException e) {
        return Status.ALREADY_EXISTS.withCause(e);
    }

    @GrpcExceptionHandler
    public Status handleNotLoginException(UnauthorizedException e) {
        return Status.UNAUTHENTICATED.withCause(e);
    }

    @GrpcExceptionHandler
    public Status handleUserNotFoundException(UserNotFoundException e) {
        return Status.NOT_FOUND.withCause(e);
    }

}
