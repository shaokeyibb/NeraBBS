package io.hikarilan.nerabbs.services.oss.handler;

import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.apache.tomcat.util.http.fileupload.FileUploadException;

@GrpcAdvice
public class GrpcExceptionAdvice {

    @GrpcExceptionHandler
    public Status fileUploadException(FileUploadException e) {
        return Status.INVALID_ARGUMENT.withCause(e);
    }

    @GrpcExceptionHandler
    public Status illegalArgumentException(IllegalArgumentException e) {
        return Status.INVALID_ARGUMENT.withCause(e);
    }

}
