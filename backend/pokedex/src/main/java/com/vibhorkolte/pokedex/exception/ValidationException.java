package com.vibhorkolte.pokedex.exception;

import com.vibhorkolte.pokedex.constants.PokedexErrorConstants;

public class ValidationException extends RuntimeException {

    private final int httpStatus;
    private final String errorCode;
    private final String errorMessage;

    public ValidationException(PokedexErrorConstants pokedexErrorConstants) {
        this.httpStatus = pokedexErrorConstants.getHttpsCode();
        this.errorCode = pokedexErrorConstants.getStatusCode();
        this.errorMessage = pokedexErrorConstants.getMessage();
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}