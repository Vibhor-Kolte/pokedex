package com.vibhorkolte.pokedex.model;

import com.vibhorkolte.pokedex.entities.Pokemon;
import lombok.Data;

import java.util.List;

@Data
public class PokemonListResponse {
    private int count;
    private String next;
    private String previous;
    private List<Pokemon> results;
}
