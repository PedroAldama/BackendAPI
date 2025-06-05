package com.backendapi.controllers;

import com.backendapi.dto.responsedto.DTOTeamResponse;
import com.backendapi.services.team.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping
    public DTOTeamResponse team(){
        return teamService.showTeam("Hikari");
    }

    @PostMapping
    public String createTeam(@RequestParam String name){
        return teamService.createTeam("Hikari",name);
    }

    @PostMapping("/add")
    public String addTeam(@RequestParam long idPokemon){
        return teamService.addPokemonToTeam("Hikari",idPokemon);
    }

    @PostMapping("/change")
    public String changeTeam(@RequestParam long idPokemonPc,@RequestParam long idPokemonTeam){
        return teamService.changePcPokemonToTeam("Hikari",idPokemonPc,idPokemonTeam);
    }
}
