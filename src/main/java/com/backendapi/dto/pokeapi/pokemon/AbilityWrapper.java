package com.backendapi.dto.pokeapi.pokemon;

import lombok.Data;

@Data
public class AbilityWrapper {
    private Ability ability;

    @Data
    public static class Ability {
        private String name;
    }
}