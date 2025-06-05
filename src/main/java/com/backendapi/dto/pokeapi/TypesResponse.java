package com.backendapi.dto.pokeapi;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TypesResponse {
    private List<PokemonWrapper> pokemon;

    @Data
    private static class PokemonWrapper {
        private Pokemon pokemon;
        @Data
        private static class Pokemon {
            private String name;
        }
    }
}
