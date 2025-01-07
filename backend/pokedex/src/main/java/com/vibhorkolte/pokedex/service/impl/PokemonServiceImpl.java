package com.vibhorkolte.pokedex.service.impl;

import com.vibhorkolte.pokedex.entities.Pokemon;
import com.vibhorkolte.pokedex.entities.PokemonDetails;
import com.vibhorkolte.pokedex.repository.PokemonRepository;
import com.vibhorkolte.pokedex.service.PokemonService;
import com.vibhorkolte.pokedex.util.PokeApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class PokemonServiceImpl implements PokemonService {

    private final WebClient webClient = WebClient.create("https://pokeapi.co/api/v2");

    @Autowired
    private PokemonRepository pokemonRepository;

    @Autowired
    private PokeApiClient pokeApiClient;

    @Override
    public Flux<Pokemon> fetchPokemonList(int offset, int limit) {
        return pokeApiClient.fetchPokemonList(offset, limit)
                .flatMapMany(response -> Flux.fromIterable(response.getResults()))
                .doOnError(error -> log.error("Error fetching Pokemon list", error));
    }

    @Override
    public Mono<PokemonDetails> fetchPokemonDetails(String name) {
        return pokemonRepository.findByName(name)
                .doOnNext(details -> log.info("Pokemon details found in cache for: {}", name))
                .switchIfEmpty(
                        Mono.defer(() -> {
                            log.info("Calling PokeAPI as Pokemon details not found in cache for: {}", name);
                            return pokeApiClient.fetchPokemonDetails(name)
                                    .doOnNext(details -> log.info("Successfully fetched Pokemon details from PokeAPI for: {}", name))
                                    .flatMap(details -> pokemonRepository.save(name, details)
                                            .doOnSuccess(saved -> log.info("Pokemon details cached for: {}", name))
                                            .thenReturn(details));
                        })
                )
                .doOnError(error -> log.error("Error fetching Pokemon detail for: {}", name, error));
    }
}
