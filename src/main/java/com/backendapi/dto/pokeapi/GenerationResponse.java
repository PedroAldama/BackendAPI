package com.backendapi.dto.pokeapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GenerationResponse {
    @JsonProperty("pokemon_species")
    private List<PokemonSpecies> pokemonSpecies;

    @Data
    public static class PokemonSpecies {
        private String name;
        private String url;
    }

}
