package com.backendapi.dto.pokeapi.pokemon;

import lombok.Data;

@Data
public class TypesWrapper{
    private Type type;

    @Data
    public static class Type {
        private String name;
    }
}
