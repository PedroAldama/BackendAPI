package com.backendapi.services.team;

import com.backendapi.dto.responsedto.DTOTeamResponse;

public interface TeamService {
    DTOTeamResponse showTeam();
    String createTeam(String name);
    String addPokemonToTeam(long idPokemon);
    String changePcPokemonToTeam(long idPokemonPc, long idPokemonTeam);
}
