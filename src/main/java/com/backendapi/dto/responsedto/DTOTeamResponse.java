package com.backendapi.dto.responsedto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

@Data
@Builder
@Setter
@Getter
public class DTOTeamResponse {
    private String name;
    private Set<DTOPokemonUserResponse> membersTeam;
}
