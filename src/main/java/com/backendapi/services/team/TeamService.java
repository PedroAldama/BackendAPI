package com.backendapi.services.team;

import com.backendapi.dto.responsedto.DTOTeamResponse;

public interface TeamService {
    DTOTeamResponse showTeam(String user);
    String createTeam(String user, String name);
    String addPokemonToTeam(String user, long idPokemon);
    String changePcPokemonToTeam(String user, long idPokemonPc, long idPokemonTeam);
}
