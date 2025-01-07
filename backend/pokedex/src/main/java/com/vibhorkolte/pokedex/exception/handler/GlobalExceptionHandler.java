package com.vibhorkolte.pokedex.exception.handler;

import com.vibhorkolte.pokedex.constants.PokedexErrorConstants;
import com.vibhorkolte.pokedex.exception.PokedexException;
import com.vibhorkolte.pokedex.exception.ValidationException;
import com.vibhorkolte.pokedex.exception.response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        StringBuilder errorMessage = new StringBuilder();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String message = error.getDefaultMessage();
            errorMessage.append(message).append("; ");
        });

        ErrorResponse errorResponse = new ErrorResponse(PokedexErrorConstants.BAD_REQUEST.getStatusCode(), errorMessage.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PokedexException.class)
    public ResponseEntity<ErrorResponse> handlePaymentException(PokedexException ex) {
        HttpStatusCode httpStatusCode = HttpStatus.resolve(ex.getHttpStatus());
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getErrorMessage());
        return ResponseEntity.status(httpStatusCode).body(errorResponse);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        HttpStatusCode httpStatusCode = HttpStatus.resolve(ex.getHttpStatus());
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getErrorMessage());
        return ResponseEntity.status(httpStatusCode).body(errorResponse);
    }

}
