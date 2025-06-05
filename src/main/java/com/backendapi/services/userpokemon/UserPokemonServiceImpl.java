package com.backendapi.services.userpokemon;

import com.backendapi.dto.pokeapi.pokemon.PokemonResponse;
import com.backendapi.dto.responsedto.DTOPokemonUserResponse;
import com.backendapi.entities.UserPokemon;
import com.backendapi.entities.Users;
import com.backendapi.repositories.UserPokemonRepository;
import com.backendapi.services.bag.BagService;
import com.backendapi.services.pokeapi.PokeAPIService;
import com.backendapi.services.users.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.backendapi.utils.Mapper.*;
import java.util.List;
import static com.backendapi.utils.VerifyItemType.verifyItemType;
@Service
@RequiredArgsConstructor
public class UserPokemonServiceImpl implements UserPokemonService {

    private final PokeAPIService pokeAPIService;
    private final UserPokemonRepository userPokemonRepository;
    private final UsersService usersService;
    private final BagService bagService;

    @Override
    public String caughtPokemon(String user,String name) {
        int probability = (int) (Math.random() * 255);
        int caughtRatio = pokeAPIService.getCaughtProbability(name);
        if(probability <= caughtRatio) {
            Users userDB = usersService.getUserByUsername(user);
            PokemonResponse pokemon = pokeAPIService.getPokemonByName(name);
            UserPokemon userPokemon = UserPokemon.builder()
                    .idPokemon(pokemon.getId())
                    .user(userDB)
                    .currentExperience(pokemon.getBaseExperience())
                    .maxExperience(pokemon.getBaseExperience())
                    .currentLife(pokemon.getStats().getFirst().getBaseStat())
                    .maxLife(pokemon.getStats().getFirst().getBaseStat())
                    .nature("Serene")
                    .name(pokemon.getName())
                    .originalTrainer(userDB.getId())
                    .status("PC")
                    .build();
            userPokemonRepository.save(userPokemon);

            return "Congratulations!! You have caught " + name + ", its id is: " + 1;
        }
        return "Oh no, " + name  +" has escaped";
    }

    @Override
    public List<DTOPokemonUserResponse> listAllUserPokemon(String user) {
        List<UserPokemon> pokemonList = userPokemonRepository.findAllByUser(getUserByUsername(user));
        return listUserPokemonToDTOPokemonUserResponse(pokemonList);
    }

    @Override
    public List<DTOPokemonUserResponse> listAllUserPokemonByStatus(String user, String status) {
        List<UserPokemon> pokemonList = userPokemonRepository.findByUserAndStatus(getUserByUsername(user),status);
        return listUserPokemonToDTOPokemonUserResponse(pokemonList);
    }

    @Override
    public DTOPokemonUserResponse getUserPokemonById(String user, int id) {
            UserPokemon userPokemon = getUserPokemon(user, id);
        return userPokemonToDTOPokemonUserResponse(userPokemon);
    }

    @Override
    @Transactional
    public String giveItemToUserPokemon(String user, int id, String item) {
        UserPokemon pokemon = getUserPokemon(user, id);
        String type = verifyItemType(item);
        if(bagService.removeItemFromBag(user,type,item)){
            if(type.equalsIgnoreCase("Consumable")){
                long life = pokemon.getCurrentLife();
                long newLife = life + 30;//Actualmente todas las berry o consumable curan solo 30
                pokemon.setCurrentLife(Math.min(newLife,pokemon.getMaxLife()));
                userPokemonRepository.save(pokemon);
                return item + " has been given to " + pokemon.getName() + " and current life is " + pokemon.getCurrentLife();
            }
            if(!pokemon.getItem().isEmpty()) {
                bagService.addItemToBag(user,verifyItemType(pokemon.getItem()),pokemon.getItem());
            }
            pokemon.setItem(item);
            userPokemonRepository.save(pokemon);
            return item + " has been given to " + pokemon.getName();
        }
        return "You don't have " + item + " in this bag";
    }

    @Override
    @Transactional
    public String getItemFromUserPokemon(String user, int id) {
        UserPokemon pokemon = getUserPokemon(user, id);
        String takenItem = pokemon.getItem();
        String response = bagService.addItemToBag(user,verifyItemType(pokemon.getItem()),pokemon.getItem());
        pokemon.setItem("");
        userPokemonRepository.save(pokemon);
        return response + "  " + takenItem + " has been taken from " + pokemon.getName() + " and now it's in your bag";
    }

    private Users getUserByUsername(String username) {
        return usersService.getUserByUsername(username);
    }
    private UserPokemon getUserPokemon(String user, int id) {
        return userPokemonRepository.findById((long) id).orElseThrow();
    }
}
