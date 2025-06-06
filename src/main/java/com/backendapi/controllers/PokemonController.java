package com.backendapi.controllers;

import com.backendapi.dto.pokeapi.TypesResponse;
import com.backendapi.services.pokeapi.PokeAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pokemon")
@RequiredArgsConstructor
public class PokemonController {
    private final PokeAPIService pokeAPIService;

    @GetMapping
    public Mono<List<String>> getGeneration(@RequestParam String generation){
        return pokeAPIService.getAllPokemonByGeneration(generation);
    }

    @GetMapping("/types")
    public TypesResponse getAllByTypes(@RequestParam String types){
        return pokeAPIService.getAllPokemonByType(types);
    }
    @GetMapping("/generation")
    Map<Integer,String> getAllPokemonIdAndNameByGeneration(@RequestParam String generation){
        return pokeAPIService.getAllPokemonIdAndNameByGeneration(generation);
    }

    @GetMapping("/evolution")
    String getEvolutionTrigger(@RequestParam String id){
        return pokeAPIService.getEvolutionTrigger(id);
    }

    @GetMapping("/random")
    String getRandomPokemon(){
        return pokeAPIService.getRandomPokemon();
    }

}
