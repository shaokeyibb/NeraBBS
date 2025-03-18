package io.hikarilan.nerabbs.services.search.handler;

import com.meilisearch.sdk.exceptions.MeilisearchApiException;
import io.hikarilan.nerabbs.common.data.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {MeilisearchApiException.class})
    public ResponseEntity<ErrorMessage> notLoginException(MeilisearchApiException e) {
        if ("index_not_found".equals(e.getCode())) {
            return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
