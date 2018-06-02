package org.shboland.api.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URISyntaxException;

@ControllerAdvice
public class ResourceURISyntaxExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { URISyntaxException.class })
    public ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
        String bodyOfResponse = "There seems to be a problem with application. Please try again.";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
