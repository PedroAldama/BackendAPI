package com.backendapi.dto.pokeapi.evolution;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Chain {
    @JsonProperty("evolves_to")
    private List<EvolvesTo> evolvesTo;
    private Species species;
}
