package com.backendapi.controllers;

import com.backendapi.dto.requestdto.CatchPokemonRequest;
import com.backendapi.dto.requestdto.PokemonStatusRequest;
import com.backendapi.dto.responsedto.DTOPokemonUserResponse;
import com.backendapi.services.userpokemon.UserPokemonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserPokemonController {
    private final UserPokemonService userPokemonService;

    @GetMapping
    public List<DTOPokemonUserResponse> getAllUserPokemon() {
        return userPokemonService.listAllUserPokemon("Hikari");
    }
    @GetMapping("/status")
    public List<DTOPokemonUserResponse> getAllUserPokemon(@RequestParam String status) {
        return userPokemonService.listAllUserPokemonByStatus("Hikari",status);
    }
    @GetMapping("/pokemon")
    public DTOPokemonUserResponse getUserPokemon(@RequestParam int id) {
        return userPokemonService.getUserPokemonById("Hikari",id);
    }
    @PostMapping("/caught")
    public String caughtPokemon(@RequestParam String pokemon) {
        return userPokemonService.caughtPokemon("Hikari",pokemon);
    }

    @PostMapping("/item/add")
    public String giveItem(@RequestParam String item, @RequestParam int pokemonId) {
        return userPokemonService.giveItemToUserPokemon("Hikari",pokemonId,item);
    }

    @PatchMapping("/item/remove")
    public String takeItem(@RequestParam int pokemonId) {
        return userPokemonService.getItemFromUserPokemon("Hikari",pokemonId);
    }
}
