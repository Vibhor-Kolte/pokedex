package com.vibhorkolte.pokedex.repository.impl;

import com.vibhorkolte.pokedex.entities.PokemonDetails;
import com.vibhorkolte.pokedex.repository.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class PokemonRepositoryImpl implements PokemonRepository {

    @Autowired
    private final ReactiveRedisTemplate<String, Object> redisOperations;

    @Autowired
    public PokemonRepositoryImpl(ReactiveRedisTemplate<String, Object> redisOperations) {
        this.redisOperations = redisOperations;
    }

    @Override
    public Mono<PokemonDetails> findByName(String name) {
        return redisOperations.opsForValue().get(name).cast(PokemonDetails.class);
    }

    @Override
    public Mono<Boolean> save(String name, PokemonDetails details) {
        return redisOperations.opsForValue()
                .set(name, details)
                .thenReturn(true);
    }
}

