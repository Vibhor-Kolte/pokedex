package com.vibhorkolte.pokedex.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PokedexErrorConstants {

    BAD_REQUEST("Bad Request","ERR_DEX_001",400),

    POKEMON_NOT_FOUND("Pokemon Not Found","ERR_DEX_002",500),

    POKEMON_LIST_NOT_FETCHED("Pokemon List not able to fetch from PokeApi", "ERR_DEX_003", 404),

    GENERIC_EXCEPTION("Some error occurred", "ERR_DEX_004", 500),

    POKEAPI_DOWNTIME("Pokeapi down at the moment, please try later", "ERR_DEX_005", 500),

    POKEAPI_UNAUTHORIZED_EXCEPTION("Pokeapi Unauthorized error", "ERR_DEX_006", 500),

    POKEAPI_FORBIDDEN_EXCEPTION("PokeApi Forbidden error", "ERR_DEX_007", 500),

    POKEAPI_SERVER_ERROR("Some error occurred at PokeApi", "ERR_DEX_008", 500),

    ;

    private final String message;

    private final String statusCode;

    private final Integer httpsCode;

}
