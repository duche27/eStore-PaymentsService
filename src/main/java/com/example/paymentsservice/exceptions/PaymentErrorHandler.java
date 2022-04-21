package com.example.paymentsservice.exceptions;

import org.axonframework.commandhandling.CommandExecutionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class PaymentErrorHandler {

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<ErrorMessage> noDataErrorHandler(NoDataFoundException e) {

        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());

        return new ResponseEntity<>(errorMessage, HttpStatus.NO_CONTENT);
    }

    // excepciones del AGGREGATE
    @ExceptionHandler(value = {CommandExecutionException.class})
    public ResponseEntity<ErrorMessage> handleCommandExecutionException(CommandExecutionException e, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {IllegalStateException.class})
    public ResponseEntity<ErrorMessage> handleIllegalStateException(IllegalStateException e) {

        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // para manejar excepciones de validaciones de constraints de hibernate
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> dataIntegrityViolationHandler(DataIntegrityViolationException e) {

        ErrorMessage error = new ErrorMessage(e.getMostSpecificCause().getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // para manejar excepciones de validaciones de hibernate con @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> fieldsValidationExceptions(MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(ErrorMessage.builder().message(errors.toString()).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<ErrorMessage> handleRuntimeException(RuntimeException e) {

        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorMessage> handleOtherException(Exception e, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
