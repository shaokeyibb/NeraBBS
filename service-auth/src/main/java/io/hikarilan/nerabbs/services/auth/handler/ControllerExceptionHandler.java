package io.hikarilan.nerabbs.services.auth.handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import io.grpc.StatusRuntimeException;
import io.hikarilan.nerabbs.common.data.ErrorMessage;
import io.hikarilan.nerabbs.services.auth.exception.UserInfoMismatchException;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(value = {NotLoginException.class})
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
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage statusRuntimeException() {
        return new ErrorMessage("Bad request.");
    }

}
