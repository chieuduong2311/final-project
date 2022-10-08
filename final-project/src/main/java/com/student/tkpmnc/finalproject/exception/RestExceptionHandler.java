package com.student.tkpmnc.finalproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({ NotFoundException.class })
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(WebRequest request, Exception e) {
        return ErrorResponse.builder().errorMessage(e.getMessage()).build();
    }

    @ExceptionHandler({ RequestException.class })
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRequestException(WebRequest request, Exception e) {
        return ErrorResponse.builder().errorMessage(e.getMessage()).build();
    }
}
