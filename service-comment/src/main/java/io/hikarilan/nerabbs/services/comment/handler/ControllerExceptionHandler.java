package io.hikarilan.nerabbs.services.comment.handler;

import io.hikarilan.nerabbs.common.data.ErrorMessage;
import io.hikarilan.nerabbs.common.exception.ForbiddenException;
import io.hikarilan.nerabbs.common.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage illegalArgumentException(IllegalArgumentException e) {
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler(value = {ForbiddenException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorMessage forbiddenException() {
        return new ErrorMessage("You do not have permission to access this resource.");
    }
}
