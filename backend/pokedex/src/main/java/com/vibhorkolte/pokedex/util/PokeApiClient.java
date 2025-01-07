package com.vibhorkolte.pokedex.util;

import com.vibhorkolte.pokedex.entities.PokemonDetails;
import com.vibhorkolte.pokedex.model.PokemonListResponse;
import reactor.core.publisher.Mono;

public interface PokeApiClient {
    Mono<PokemonListResponse> fetchPokemonList(int offset, int limit);
    Mono<PokemonDetails> fetchPokemonDetails(String name);
}