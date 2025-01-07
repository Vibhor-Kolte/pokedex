package com.vibhorkolte.pokedex.exception;

import com.vibhorkolte.pokedex.constants.PokedexErrorConstants;

public class PokedexException extends RuntimeException {

    private final int httpStatus;
    private final String errorCode;
    private final String errorMessage;

    public PokedexException(PokedexErrorConstants pokedexErrorConstants) {
        super(pokedexErrorConstants.getMessage());
        this.httpStatus = pokedexErrorConstants.getHttpsCode();
        this.errorCode = pokedexErrorConstants.getStatusCode();
        errorMessage = pokedexErrorConstants.getMessage();
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
