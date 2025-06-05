package com.backendapi.services.userpokemon;

import com.backendapi.dto.responsedto.DTOPokemonUserResponse;

import java.util.List;

public interface UserPokemonService {
    String caughtPokemon(String user, String name);
    List<DTOPokemonUserResponse> listAllUserPokemon(String user);
    List<DTOPokemonUserResponse> listAllUserPokemonByStatus(String user,String status);
    DTOPokemonUserResponse getUserPokemonById(String user, int id);
    String giveItemToUserPokemon(String user, int id, String item);
    String getItemFromUserPokemon(String user, int id);
    String changePokemonName(String user, String newName, long idPokemon);
    void changeStatus(String user, String newStatus, long idPokemon);
}
