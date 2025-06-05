package com.backendapi.dto.pokeapi.pokemon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class PokemonResponse {
    private int id;
    private String name;
    @JsonProperty("base_experience")
    private int baseExperience;
    private int height;
    private int weight;

    private List<AbilityWrapper> abilities;
    private List<TypesWrapper> types;

}
