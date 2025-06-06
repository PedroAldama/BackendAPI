package com.backendapi.controllers;

import com.backendapi.services.pokeapi.PokeAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/pokemon")
@RequiredArgsConstructor
public class PokemonController {
    private final PokeAPIService pokeAPIService;

    @GetMapping
    public Mono<List<String>> getGeneration(@RequestParam String generation){
        return pokeAPIService.getAllPokemonByGeneration(generation);
    }

}
