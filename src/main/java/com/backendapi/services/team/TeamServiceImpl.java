package com.backendapi.services.team;

import com.backendapi.documents.Team;
import com.backendapi.dto.responsedto.DTOPokemonUserResponse;
import com.backendapi.dto.responsedto.DTOTeamResponse;
import com.backendapi.repositories.TeamRepository;
import com.backendapi.services.userpokemon.UserPokemonService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.backendapi.utils.SecurityUtils.getAuthenticatedUsername;
import java.util.HashSet;
import java.util.Set;

import static com.backendapi.utils.Mapper.teamToDTOTeamResponse;
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService{

    private final TeamRepository teamRepository;
    private final UserPokemonService userPokemonService;

    @Override
    @Transactional(readOnly = true)
    public DTOTeamResponse showTeam() {
        Team team = getTeam(getUser());
        DTOTeamResponse dtoTeamResponse = teamToDTOTeamResponse(team);
        Set<DTOPokemonUserResponse> teamPokemon = new HashSet<>();
        for(long id: team.getIdPokemon()){
            teamPokemon.add(userPokemonService.getUserPokemonById((int) id));
        }
        dtoTeamResponse.setMembersTeam(teamPokemon);
        return  dtoTeamResponse;
    }

    @Override
    @Transactional
    public String createTeam( String name) {
        if(teamRepository.existsById(getUser())){
            return "You already have a team";
        }
        Team team = Team.builder().id(getUser()).name(name).idPokemon(new HashSet<>()).build();
        teamRepository.save(team);
        return "Congratulations! You have created a new team, Team: " + team.getName();
    }

    @Override
    @Transactional
    public String addPokemonToTeam(long idPokemon) {
        Team team = getTeam(getUser());
        if(team.getIdPokemon().size() >= 6){
            return "You already have 6 Pokemon in your team";
        }
        DTOPokemonUserResponse pokemon = userPokemonService.getUserPokemonById((int)idPokemon);
        if(team.getIdPokemon().add(idPokemon)){
            teamRepository.save(team);
            userPokemonService.changeStatus("Team",idPokemon);
            return pokemon.getName() + " is now added to your team";
        }
        return "You already have this Pokemon in your team";
    }

    @Override
    @Transactional
    public String changePcPokemonToTeam(long idPokemonPc, long idPokemonTeam) {
        Team team = getTeam(getUser());
        DTOPokemonUserResponse pokemonPc = userPokemonService.getUserPokemonById((int)idPokemonPc);
        if(pokemonPc.getStatus().equalsIgnoreCase("PC")){
            if(team.getIdPokemon().remove(idPokemonTeam)){
                userPokemonService.changeStatus("Team",idPokemonPc);
                userPokemonService.changeStatus("PC",idPokemonTeam);
                team.getIdPokemon().add(idPokemonPc);
                teamRepository.save(team);
                return "Pokemon has been changed";
            }else {
                return "You don't have this Pokemon in your team " + idPokemonTeam;
            }
        }
        return "This Pokémon is not available on PC " + pokemonPc.getStatus();
    }

    private Team getTeam(String user){
        return teamRepository.findById(user).orElseThrow(()->new EntityNotFoundException(user));
    }
    private String getUser(){
        return getAuthenticatedUsername();
    }
}
