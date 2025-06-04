package com.backendapi.dto.responsedto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class DTOPokemonResponse {
    private long id;
    private String name;
    private long baseExp;
    private int height;
    private int weight;
    private int life;
    private String evolutionMethod;
    private Set<String> types;
    private Set<String> abilities;

}
