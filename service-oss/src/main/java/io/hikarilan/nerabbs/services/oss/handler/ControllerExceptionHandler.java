package io.hikarilan.nerabbs.services.oss.handler;

import io.hikarilan.nerabbs.common.data.ErrorMessage;
import io.hikarilan.nerabbs.common.exception.UnauthorizedException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMessage notLoginException() {
        return new ErrorMessage("Not logged in.");
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

}
