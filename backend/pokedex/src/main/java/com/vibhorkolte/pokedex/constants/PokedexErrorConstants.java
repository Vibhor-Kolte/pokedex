package com.vibhorkolte.pokedex.constants;

public enum PokedexErrorConstants {

    BAD_REQUEST("Bad Request","ERR_DEX_001",400),

    POKEMON_NOT_FOUND("Pokemon Not Found","ERR_DEX_002",500),
    ;

    private final String message;

    private final String statusCode;

    private final Integer httpsCode;

    PokedexErrorConstants(String message, String statusCode, Integer httpsCode) {
        this.message = message;
        this.statusCode = statusCode;
        this.httpsCode = httpsCode;
    }

    public String getMessage() {
        return message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public Integer getHttpsCode() {
        return httpsCode;
    }
}
