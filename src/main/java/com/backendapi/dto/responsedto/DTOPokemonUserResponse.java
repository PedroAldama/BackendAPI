package com.backendapi.dto.responsedto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DTOPokemonUserResponse {
    private Long idPokemon;
    private String name;
    private String status;
    private String item;
    private String nature;
    private String life;
    private long originalTrainer;
    private String exp;
}
