package io.hikarilan.nerabbs.services.post.handler;

import io.grpc.StatusRuntimeException;
import io.hikarilan.nerabbs.common.data.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {NoSuchElementException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage notFoundException() {
        return new ErrorMessage("The resource you requested isn't exists.");
    }

    @ExceptionHandler(value = {StatusRuntimeException.class})
    public ResponseEntity<ErrorMessage> statusRuntimeException(StatusRuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage(e.getMessage()));
    }
}
