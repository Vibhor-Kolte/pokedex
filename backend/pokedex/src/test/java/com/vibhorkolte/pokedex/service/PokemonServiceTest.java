package com.vibhorkolte.pokedex.service;

import static com.vibhorkolte.pokedex.constants.PokedexErrorConstants.POKEAPI_DOWNTIME;
import static org.mockito.Mockito.*;

import com.vibhorkolte.pokedex.entities.PokemonDetails;
import com.vibhorkolte.pokedex.exception.PokeApiException;
import com.vibhorkolte.pokedex.repository.PokemonRepository;
import com.vibhorkolte.pokedex.service.impl.PokemonServiceImpl;
import com.vibhorkolte.pokedex.util.PokeApiClient;
import io.lettuce.core.RedisException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class PokemonServiceTest {

    @Mock
    private PokemonRepository pokemonRepository;

    @Mock
    private PokeApiClient pokeApiClient;

    @InjectMocks
    private PokemonServiceImpl pokemonServiceImpl;

    private String pokemonName;
    private PokemonDetails pokemonDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pokemonName = "pikachu";
        pokemonDetails = PokemonDetails.builder()
                .name(pokemonName)
                .weight(60)
                .height(4)
                .build();

        when(pokemonRepository.findByName(pokemonName)).thenReturn(Mono.empty()); // Simulate cache miss
        when(pokeApiClient.fetchPokemonDetails(pokemonName)).thenReturn(Mono.just(pokemonDetails)); // Simulate PokeAPI call success
    }

    @Test
    void testFetchPokemonDetails_whenNotFoundInCache_callsPokeApiAndReturnsDetails() {
        // Setup expected behavior when the Pokemon is not in cache
        when(pokemonRepository.save(pokemonName, pokemonDetails)).thenReturn(Mono.just(true));

        Mono<PokemonDetails> result = pokemonServiceImpl.fetchPokemonDetails(pokemonName);

        StepVerifier.create(result)
                .expectNext(pokemonDetails)
                .verifyComplete();

        verify(pokeApiClient, times(1)).fetchPokemonDetails(pokemonName);
        verify(pokemonRepository, times(1)).save(pokemonName, pokemonDetails);
    }

    @Test
    void testFetchPokemonDetails_whenCacheFails_callsPokeApiAndContinues() {
        // Simulate Redis cache save failure
        when(pokemonRepository.save(pokemonName, pokemonDetails)).thenReturn(Mono.error(new RedisException("Redis save failed")));

        Mono<PokemonDetails> result = pokemonServiceImpl.fetchPokemonDetails(pokemonName);

        StepVerifier.create(result)
                .expectNext(pokemonDetails) // The result should still return PokemonDetails despite cache failure
                .verifyComplete();

        verify(pokeApiClient, times(1)).fetchPokemonDetails(pokemonName);
        verify(pokemonRepository, times(1)).save(pokemonName, pokemonDetails); // Verifying that we attempt to cache the result
    }

    @Test
    void testFetchPokemonDetails_whenCacheExists_returnsCachedDetails() {
        // Simulate cache hit
        when(pokemonRepository.findByName(pokemonName)).thenReturn(Mono.just(pokemonDetails));

        Mono<PokemonDetails> result = pokemonServiceImpl.fetchPokemonDetails(pokemonName);

        StepVerifier.create(result)
                .expectNext(pokemonDetails)
                .verifyComplete();

        verify(pokeApiClient, never()).fetchPokemonDetails(pokemonName); // PokeApiClient should not be called
        verify(pokemonRepository, never()).save(pokemonName, pokemonDetails); // Repository should not be called
    }

    @Test
    void testFetchPokemonDetails_whenPokeApiFails_returnsError() {
        // Simulate PokeAPI failure
        when(pokeApiClient.fetchPokemonDetails(pokemonName)).thenReturn(Mono.error(new PokeApiException(POKEAPI_DOWNTIME)));

        Mono<PokemonDetails> result = pokemonServiceImpl.fetchPokemonDetails(pokemonName);

        StepVerifier.create(result)
                .expectError(PokeApiException.class) // Expect the exception to propagate
                .verify();

        verify(pokemonRepository, never()).save(anyString(), any()); // Cache operation should not be attempted
    }

    @Test
    void testFetchPokemonDetails_whenRedisFails_returnsError() {
        // Simulate Redis error while saving
        when(pokemonRepository.findByName(pokemonName)).thenReturn(Mono.empty());
        when(pokeApiClient.fetchPokemonDetails(pokemonName)).thenReturn(Mono.just(pokemonDetails));
        when(pokemonRepository.save(pokemonName, pokemonDetails)).thenReturn(Mono.error(new RedisException("Redis save failed")));

        Mono<PokemonDetails> result = pokemonServiceImpl.fetchPokemonDetails(pokemonName);

        StepVerifier.create(result)
                .expectNext(pokemonDetails) // The result should still return the details from PokeAPI
                .verifyComplete();

        verify(pokemonRepository, times(1)).save(pokemonName, pokemonDetails); // Save should be attempted, even if it fails
    }
}

