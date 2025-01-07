package com.vibhorkolte.pokedex.repository;

import com.vibhorkolte.pokedex.entities.PokemonDetails;
import reactor.core.publisher.Mono;

public interface PokemonRepository {
    Mono<PokemonDetails> findByName(String name);
    Mono<Boolean> save(String name, PokemonDetails details);
}
