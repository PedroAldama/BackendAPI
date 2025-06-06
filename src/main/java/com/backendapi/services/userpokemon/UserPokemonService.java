package com.backendapi.services.userpokemon;

import com.backendapi.dto.responsedto.DTOPokemonUserResponse;

import java.util.List;

public interface UserPokemonService {
    String caughtPokemon(String name);
    List<DTOPokemonUserResponse> listAllUserPokemon();
    List<DTOPokemonUserResponse> listAllUserPokemonByStatus(String status);
    DTOPokemonUserResponse getUserPokemonById(int id);
    String giveItemToUserPokemon(int id, String item);
    String getItemFromUserPokemon(int id);
    String changePokemonName(String newName, long idPokemon);
    void changeStatus(String newStatus, long idPokemon);
    String setPokemonInDayCare(long idPokemon);
    String getPokemonFromDayCare(long idPokemon);

}
