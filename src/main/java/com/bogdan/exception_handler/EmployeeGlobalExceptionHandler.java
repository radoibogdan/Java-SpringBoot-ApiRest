package com.bogdan.exception_handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestController
@ControllerAdvice // interceptor of exceptions thrown by methods annotated with @RequestMappin
public class EmployeeGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception ex, WebRequest webRequest) {
        EmployeeExceptionResponse exception = new EmployeeExceptionResponse(
                ex.getMessage(),
                webRequest.getDescription(false),
                new Date()
        );
        return new ResponseEntity<Object>(exception, HttpStatus.INTERNAL_SERVER_ERROR); // Code 500
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Object> handleEmployeeNotFoundException(Exception ex, WebRequest request) {
        EmployeeExceptionResponse exception = new EmployeeExceptionResponse(
                ex.getMessage(),
                request.getDescription(false),
                new Date());
        return new ResponseEntity<Object>(exception, HttpStatus.NOT_FOUND); // Code 404
    }

    // Exception levée lors du saveUser si les arguments ne respected pas la validation des entités (@Valid)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        EmployeeExceptionResponse exception = new EmployeeExceptionResponse(
                "Les informations envoyés sont incorrectes", // message
                ex.getBindingResult().toString(),                       // description
                new Date());                                            // date
        return new ResponseEntity<Object>(exception, HttpStatus.BAD_REQUEST); // Code 400 Bad Request
    }
}
