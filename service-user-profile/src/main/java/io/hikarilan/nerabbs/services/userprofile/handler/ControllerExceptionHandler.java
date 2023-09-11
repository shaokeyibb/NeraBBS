package io.hikarilan.nerabbs.services.userprofile.handler;

import io.grpc.StatusRuntimeException;
import io.hikarilan.nerabbs.common.data.ErrorMessage;
import io.hikarilan.nerabbs.common.exception.UnauthorizedException;
import jakarta.validation.ConstraintViolationException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {UnauthorizedException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMessage notLoginException() {
        return new ErrorMessage("Not logged in.");
    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage notFoundException() {
        return new ErrorMessage("The resource you requested isn't exists.");
    }

    @ExceptionHandler(value = {FileSizeLimitExceededException.class, SizeLimitExceededException.class})
    @ResponseStatus(value = HttpStatus.PAYLOAD_TOO_LARGE)
    public ErrorMessage sizeLimitExceededException() {
        return new ErrorMessage("File size limit exceeded.");
    }

    @ExceptionHandler(value = {FileUploadException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage fileUploadException(FileUploadException e) {
        return new ErrorMessage("File upload failed. " + e.getMessage());
    }

    @ExceptionHandler(value = {StatusRuntimeException.class})
    public ResponseEntity<ErrorMessage> statusRuntimeException(StatusRuntimeException e) {
        return switch (e.getStatus().getCode()) {
            case ALREADY_EXISTS ->
                    ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMessage("The resource you requested already exists."));
            case NOT_FOUND ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("The resource you requested does not exist."));
            case UNAUTHENTICATED ->
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessage("Not logged in."));
            case INVALID_ARGUMENT -> ResponseEntity.badRequest().body(new ErrorMessage("Bad request."));
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage(e.getMessage()));
        };
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage badRequestException(IllegalArgumentException e) {
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage badRequestException(ConstraintViolationException e) {
        return new ErrorMessage(e.getMessage());
    }
}