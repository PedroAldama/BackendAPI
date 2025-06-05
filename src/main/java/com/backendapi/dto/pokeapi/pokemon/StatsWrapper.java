package com.backendapi.dto.pokeapi.pokemon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StatsWrapper {
    @JsonProperty("base_stat")
    private int baseStat;
}
