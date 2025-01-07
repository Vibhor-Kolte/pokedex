package com.vibhorkolte.pokedex.exception;

import com.vibhorkolte.pokedex.constants.PokedexErrorConstants;
import lombok.Getter;

@Getter
public class PokeApiException extends RuntimeException {

    private final int httpStatus;
    private final String errorCode;
    private final String errorMessage;

    public PokeApiException(PokedexErrorConstants pokedexErrorConstants) {
        this.httpStatus = pokedexErrorConstants.getHttpsCode();
        this.errorCode = pokedexErrorConstants.getStatusCode();
        this.errorMessage = pokedexErrorConstants.getMessage();
    }

}