package com.backendapi.dto.responsedto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DTOPokemonUserResponse {
    private Long idPokemon;
    private String name;
    private String item;
    private String nature;
    private long life;
    private String originalTrainer;
    private long exp;
}
