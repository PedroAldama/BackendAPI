package com.backendapi.utils;

import com.backendapi.documents.Bag;
import com.backendapi.documents.Shop;
import com.backendapi.documents.Team;
import com.backendapi.dto.responsedto.DTOBagResponse;
import com.backendapi.dto.responsedto.DTOPokemonUserResponse;
import com.backendapi.dto.responsedto.DTOShopResponse;
import com.backendapi.dto.responsedto.DTOTeamResponse;
import com.backendapi.entities.UserPokemon;

import java.util.ArrayList;
import java.util.List;

public class Mapper {
    private Mapper(){
        //Constructor privado, esta clase solo funciona como apoyo para convertir
    }

    public static DTOPokemonUserResponse userPokemonToDTOPokemonUserResponse(UserPokemon userPokemon) {
        return DTOPokemonUserResponse.builder()
                .idPokemon(userPokemon.getIdPokemon())
                .name(userPokemon.getName())
                .status(userPokemon.getStatus())
                .item(userPokemon.getItem())
                .nature(userPokemon.getNature())
                .life(userPokemon.getCurrentLife()+"/"+userPokemon.getMaxLife())
                .originalTrainer(userPokemon.getOriginalTrainer())
                .exp(userPokemon.getCurrentExperience() +"/"+userPokemon.getMaxExperience())
                .build();
    }
    public static List<DTOPokemonUserResponse> listUserPokemonToDTOPokemonUserResponse(List<UserPokemon> userPokemonList) {
        List<DTOPokemonUserResponse> dtoPokemonUserResponseList = new ArrayList<>();
        for (UserPokemon userPokemon : userPokemonList) {
            dtoPokemonUserResponseList.add(userPokemonToDTOPokemonUserResponse(userPokemon));
        }
        return dtoPokemonUserResponseList;
    }
    public static DTOBagResponse bagToDTOBagResponse(Bag bag) {
        return DTOBagResponse.builder()
                .money(bag.getMoney())
                .evolutionItems(bag.getEvolutionItems())
                .consumableItems(bag.getConsumableItems())
                .build();
    }
    public static DTOShopResponse shopToDTOShopResponse(Shop shop){
        return DTOShopResponse.builder()
                .consumableItems(shop.getConsumableItems())
                .evolutionItems(shop.getEvolutionItems())
                .build();
    }
    public static DTOTeamResponse teamToDTOTeamResponse(Team team){
        return DTOTeamResponse.builder().name(team.getName()).build();
    }
}
