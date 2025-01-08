package com.vibhorkolte.pokedex.controller;

import com.vibhorkolte.pokedex.entities.Pokemon;
import com.vibhorkolte.pokedex.entities.PokemonDetails;
import com.vibhorkolte.pokedex.service.PokemonService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/pokemon")
public class PokemonController {

    @Autowired
    private PokemonService pokemonService;

    @GetMapping
    @Operation(summary = "Get Pokemon List")
    public Flux<Pokemon> getPokemonList(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int limit) {
        log.info("Received getPokemonList() for offset:[{}] and limit:[{}]", offset, limit);
        return pokemonService.fetchPokemonList(offset, limit);
    }

    @GetMapping("/{name}")
    @Operation(summary = "Get Pokemon Details")
    public Mono<PokemonDetails> getPokemonDetails(@PathVariable String name) {
        log.info("Received getPokemonDetails() for {}", name);
        return pokemonService.fetchPokemonDetails(name);
    }
}
