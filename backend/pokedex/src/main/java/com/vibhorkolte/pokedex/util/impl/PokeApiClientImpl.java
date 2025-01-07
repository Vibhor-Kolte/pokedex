package com.vibhorkolte.pokedex.util.impl;

import com.vibhorkolte.pokedex.entities.PokemonDetails;
import com.vibhorkolte.pokedex.model.PokemonListResponse;
import com.vibhorkolte.pokedex.util.PokeApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
                .bodyToMono(PokemonListResponse.class);
    }

    @Override
    public Mono<PokemonDetails> fetchPokemonDetails(String name) {
        return webClient.get()
                .uri(pokemonEndpoint + name)
                .retrieve()
                .bodyToMono(PokemonDetails.class);
    }
}

