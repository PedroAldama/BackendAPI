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
    public DTOTeamResponse showTeam(String user) {
        Team team = getTeam(user);
        DTOTeamResponse dtoTeamResponse = teamToDTOTeamResponse(team);
        Set<DTOPokemonUserResponse> teamPokemon = new HashSet<>();
        for(long id: team.getIdPokemon()){
            teamPokemon.add(userPokemonService.getUserPokemonById(user,(int) id));
        }
        dtoTeamResponse.setMembersTeam(teamPokemon);
        return  dtoTeamResponse;
    }

    @Override
    @Transactional
    public String createTeam(String user, String name) {
        if(teamRepository.existsById(user)){
            return "You already have a team";
        }
        Team team = Team.builder().id(user).name(name).idPokemon(new HashSet<>()).build();
        teamRepository.save(team);
        return "Congratulations! You have created a new team, Team: " + team.getName();
    }

    @Override
    @Transactional
    public String addPokemonToTeam(String user, long idPokemon) {
        Team team = getTeam(user);
        if(team.getIdPokemon().size() >= 6){
            return "You already have 6 Pokemon in your team";
        }
        DTOPokemonUserResponse pokemon = userPokemonService.getUserPokemonById(user, (int)idPokemon);
        if(team.getIdPokemon().add(idPokemon)){
            teamRepository.save(team);
            userPokemonService.changeStatus(user,"Team",idPokemon);
            return pokemon.getName() + " is now added to your team";
        }
        return "You already have this Pokemon in your team";
    }

    @Override
    @Transactional
    public String changePcPokemonToTeam(String user, long idPokemonPc, long idPokemonTeam) {
        Team team = getTeam(user);
        DTOPokemonUserResponse pokemonPc = userPokemonService.getUserPokemonById(user, (int)idPokemonPc);
        if(pokemonPc.getStatus().equalsIgnoreCase("PC")){
            if(team.getIdPokemon().remove(idPokemonTeam)){
                userPokemonService.changeStatus(user,"Team",idPokemonPc);
                userPokemonService.changeStatus(user,"PC",idPokemonTeam);
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
}
