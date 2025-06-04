package com.backendapi.dto.responsedto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class DTOTeamResponse {
    private String name;
    private Set<DTOPokemonUserResponse> membersTeam;
}
