package com.backendapi.dto.pokeapi.evolution;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public  class EvolutionDetail {
    private Trigger trigger;

    @JsonProperty("min_level")
    private int minLevel;
    private Item item;
}