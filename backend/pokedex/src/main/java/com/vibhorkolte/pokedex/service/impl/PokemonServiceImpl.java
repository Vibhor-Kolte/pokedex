package com.vibhorkolte.pokedex.service.impl;

import com.vibhorkolte.pokedex.entities.Pokemon;
import com.vibhorkolte.pokedex.entities.PokemonDetails;
import com.vibhorkolte.pokedex.model.PokemonListResponse;
import com.vibhorkolte.pokedex.service.PokemonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class PokemonServiceImpl implements PokemonService {

    private final WebClient webClient = WebClient.create("https://pokeapi.co/api/v2");

    @Override
    public Flux<Pokemon> fetchPokemonList(int offset, int limit) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/pokemon")
                        .queryParam("offset", offset)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToMono(PokemonListResponse.class)
                .flatMapMany(response -> Flux.fromIterable(response.getResults()))
                .doOnError(error -> log.error("Error fetching Pokemon list", error));
    }

    @Override
    public Mono<PokemonDetails> fetchPokemonDetails(String name) {
        return webClient.get()
                        .uri("/pokemon/{name}", name)
                        .retrieve()
                        .bodyToMono(PokemonDetails.class)
                        .doOnError(error -> log.error("Error fetching Pokemon detail", error));
    }
}
