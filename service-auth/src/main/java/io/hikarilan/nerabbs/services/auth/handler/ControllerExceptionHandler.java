package io.hikarilan.nerabbs.services.auth.handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import io.grpc.StatusRuntimeException;
import io.hikarilan.nerabbs.common.data.ErrorMessage;
import io.hikarilan.nerabbs.common.exception.UnauthorizedException;
import io.hikarilan.nerabbs.services.auth.exception.UserInfoMismatchException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {UserInfoMismatchException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMessage userInfoMismatchException() {
        return new ErrorMessage("Username or password incorrect.");
    }

    @ExceptionHandler(value = {NotLoginException.class, UnauthorizedException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMessage notLoginException() {
        return new ErrorMessage("Not logged in.");
    }

    @ExceptionHandler(value = {NotRoleException.class, NotPermissionException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorMessage notRoleException() {
        return new ErrorMessage("Not enough permissions.");
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

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage badRequestException(ConstraintViolationException e) {
        return new ErrorMessage(e.getMessage());
    }

}
