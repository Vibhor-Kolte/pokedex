package com.vibhorkolte.pokedex.service;

import com.vibhorkolte.pokedex.entities.Pokemon;
import com.vibhorkolte.pokedex.entities.PokemonDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PokemonService {

    Flux<Pokemon> fetchPokemonList(int offset, int limit);

    Mono<PokemonDetails> fetchPokemonDetails(String name);
}
