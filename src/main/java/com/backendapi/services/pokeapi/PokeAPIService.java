package com.backendapi.services.pokeapi;

import com.backendapi.dto.pokeapi.pokemon.PokemonResponse;
import com.backendapi.dto.pokeapi.TypesResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PokeAPIService {
    PokemonResponse getPokemonByName(String name);
    PokemonResponse getPokemonById(int id);
    int getPokemonId(String name);
    int getCaughtProbability(String name);
    String getEvolutionTrigger(String id);
    Set<String> getAllConsumableItems();
    Set<String> getAllEvolutionItems();
    Mono<List<String>> getAllPokemonByGeneration(String generation);
    Map<Integer,String> getAllPokemonIdAndNameByGeneration(String generation);
    TypesResponse getAllPokemonByType(String type);
    String getRandomPokemon();
}
