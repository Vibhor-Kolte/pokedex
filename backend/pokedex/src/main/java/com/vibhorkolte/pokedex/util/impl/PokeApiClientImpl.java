package com.vibhorkolte.pokedex.util.impl;

import com.vibhorkolte.pokedex.constants.PokedexErrorConstants;
import com.vibhorkolte.pokedex.entities.PokemonDetails;
import com.vibhorkolte.pokedex.exception.PokeApiException;
import com.vibhorkolte.pokedex.model.PokemonListResponse;
import com.vibhorkolte.pokedex.util.PokeApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class PokeApiClientImpl implements PokeApiClient {

    @Value("${pokeapi.pokemon-endpoint}")
    private String pokemonEndpoint;

    private final WebClient webClient;

    public PokeApiClientImpl(@Value("${pokeapi.base-url}") String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    @Override
    public Mono<PokemonListResponse> fetchPokemonList(int offset, int limit) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(pokemonEndpoint)
                        .queryParam("offset", offset)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("Error fetching Pokemon list: {} - {}", response.statusCode(), errorBody);
                                    return Mono.error(new PokeApiException(resolveError(response.statusCode(), errorBody)));
                                })
                )
                .bodyToMono(PokemonListResponse.class);
    }

    @Override
    public Mono<PokemonDetails> fetchPokemonDetails(String name) {
        return webClient.get()
                .uri(pokemonEndpoint + name)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("Error fetching Pokemon details for {}: {} - {}", name, response.statusCode(), errorBody);
                                    return Mono.error(new PokeApiException(resolveError(response.statusCode(), errorBody)));
                                })
                )
                .bodyToMono(PokemonDetails.class);
    }

    private PokedexErrorConstants resolveError(HttpStatusCode statusCode, String errorBody) {
        if (statusCode == HttpStatus.NOT_FOUND) {
            return PokedexErrorConstants.POKEMON_NOT_FOUND;
        } else if (statusCode == HttpStatus.BAD_REQUEST) {
            return PokedexErrorConstants.BAD_REQUEST;
        } else if (statusCode == HttpStatus.FORBIDDEN) {
            return PokedexErrorConstants.POKEAPI_FORBIDDEN_EXCEPTION;
        } else if (statusCode == HttpStatus.UNAUTHORIZED) {
            return PokedexErrorConstants.POKEAPI_UNAUTHORIZED_EXCEPTION;
        } else if (statusCode.is5xxServerError()) {
            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
                return PokedexErrorConstants.POKEAPI_DOWNTIME;
            } else if (statusCode == HttpStatus.SERVICE_UNAVAILABLE) {
                return PokedexErrorConstants.POKEAPI_DOWNTIME;
            }
            return PokedexErrorConstants.POKEAPI_SERVER_ERROR;
        }
        return PokedexErrorConstants.GENERIC_EXCEPTION;
    }
}

