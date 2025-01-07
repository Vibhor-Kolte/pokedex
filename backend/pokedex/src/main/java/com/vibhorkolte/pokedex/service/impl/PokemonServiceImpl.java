package com.vibhorkolte.pokedex.service.impl;

import com.vibhorkolte.pokedex.entities.Pokemon;
import com.vibhorkolte.pokedex.entities.PokemonDetails;
import com.vibhorkolte.pokedex.exception.PokeApiException;
import com.vibhorkolte.pokedex.repository.PokemonRepository;
import com.vibhorkolte.pokedex.service.PokemonService;
import com.vibhorkolte.pokedex.util.PokeApiClient;
import io.lettuce.core.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class PokemonServiceImpl implements PokemonService {

    @Autowired
    private PokemonRepository pokemonRepository;

    @Autowired
    private PokeApiClient pokeApiClient;

    @Override
    public Flux<Pokemon> fetchPokemonList(int offset, int limit) {
        return pokeApiClient.fetchPokemonList(offset, limit)
                .flatMapMany(response -> Flux.fromIterable(response.getResults()))
                .onErrorResume(PokeApiException.class, error -> {
                    log.error("PokeApiException during fetchPokemonList: {}", error.getMessage());
                    return Mono.error(error);
                });
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
                                            .onErrorResume(RedisException.class, error -> {
                                                // Log and continue even if Redis save fails
                                                log.error("Failed to cache Pokemon details in Redis for {}: {}", name, error.getMessage());
                                                return Mono.just(false); // Continue and return the details if Redis save fails
                                            })
                                            .thenReturn(details));
                        })
                )
                .onErrorResume(ex -> {
                    if (ex instanceof PokeApiException) {
                        log.error("PokeApiException during fetchPokemonDetails for {}: {}", name, ex.getMessage());
                        return Mono.error(ex);
                    } else {
                        log.error("Error fetching Pokemon details for {}: {}", name, ex.getMessage());
                        // Continue to fetch data from PokeAPI even if there is an error in Redis or repository
                        return pokeApiClient.fetchPokemonDetails(name)
                                .doOnNext(details -> log.info("Fetched Pokemon details from PokeAPI for: {}", name));
                    }
                });
    }
}
