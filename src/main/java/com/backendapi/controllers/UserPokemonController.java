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
        return userPokemonService.listAllUserPokemon();
    }
    
    @GetMapping("/status")
    public List<DTOPokemonUserResponse> getAllUserPokemon(@RequestParam String status) {
        return userPokemonService.listAllUserPokemonByStatus(status);
    }
    @GetMapping("/pokemon")
    public DTOPokemonUserResponse getUserPokemon(@RequestParam int id) {
        return userPokemonService.getUserPokemonById(id);
    }
    @PostMapping("/caught")
    public String caughtPokemon(@RequestParam String pokemon) {
        return userPokemonService.caughtPokemon(pokemon);
    }

    @PostMapping("/item/add")
    public String giveItem(@RequestParam String item, @RequestParam int pokemonId) {
        return userPokemonService.giveItemToUserPokemon(pokemonId,item);
    }

    @PatchMapping("/item/remove")
    public String takeItem(@RequestParam int pokemonId) {
        return userPokemonService.getItemFromUserPokemon(pokemonId);
    }

    @PatchMapping("/pokemon/change/name")
    public String changeName(@RequestParam int pokemonId, @RequestParam String name) {
        return userPokemonService.changePokemonName(name,pokemonId);
    }

    @PostMapping("/swap")
    public String createRoomToSwap(@RequestParam long idPokemon){
        return userPokemonService.createRoomToChange(idPokemon);
    }
    @PostMapping("/swap/other")
    public String swapPokemon(@RequestParam long idPokemon, @RequestParam String trainer){
        return userPokemonService.changePokemonWithOther(trainer,idPokemon);
    }

    @PostMapping("/daycare")
    public String putPokemonInDayCare(@RequestParam long idPokemon){
        return userPokemonService.setPokemonInDayCare(idPokemon);
    }

    @GetMapping("/daycare/collect")
    public String getPokemonFromDayCare(@RequestParam long idPokemon){
        return userPokemonService.getPokemonFromDayCare(idPokemon);
    }
}
