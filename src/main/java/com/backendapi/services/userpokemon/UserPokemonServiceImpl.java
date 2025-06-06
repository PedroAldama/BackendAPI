package com.backendapi.services.userpokemon;

import com.backendapi.dto.pokeapi.pokemon.PokemonResponse;
import com.backendapi.dto.responsedto.DTOPokemonUserResponse;
import com.backendapi.entities.UserPokemon;
import com.backendapi.entities.Users;
import com.backendapi.exceptions.PokemonException;
import com.backendapi.repositories.UserPokemonRepository;
import com.backendapi.services.bag.BagService;
import com.backendapi.services.pokeapi.PokeAPIService;
import com.backendapi.services.redis.RedisService;
import com.backendapi.services.users.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.backendapi.utils.Mapper.*;
import java.util.List;
import static com.backendapi.utils.VerifyItemType.verifyItemType;
import static com.backendapi.utils.SecurityUtils.getAuthenticatedUsername;
@Service
@RequiredArgsConstructor
public class UserPokemonServiceImpl implements UserPokemonService {

    private final PokeAPIService pokeAPIService;
    private final UserPokemonRepository userPokemonRepository;
    private final UsersService usersService;
    private final BagService bagService;
    private final RedisService redisService;

    /**
     * @author Pedro Aldama
     * @param name el nombre o id del pokemon
     * @return String se realiza un numero al azar, si este es igual o menor a la
     * probabilidad de captura del pokemon buscado en PokeApi, se agrega a la BD en mysql
     * y se le asigna a usuario, de otra manera puede seguir intentando
     */
    @Override
    @Transactional
    public String caughtPokemon(String name) {
        int probability = (int) (Math.random() * 255);
        int caughtRatio = pokeAPIService.getCaughtProbability(name);
        if(probability <= caughtRatio) {
            Users userDB = usersService.getUserByUsername(getUser());
            PokemonResponse pokemon = pokeAPIService.getPokemonByName(name);
            UserPokemon userPokemon = UserPokemon.builder()
                    .idPokemon(pokemon.getId())
                    .user(userDB)
                    .currentExperience(0)
                    .maxExperience(pokemon.getBaseExperience())
                    .currentLife(pokemon.getStats().getFirst().getBaseStat())
                    .maxLife(pokemon.getStats().getFirst().getBaseStat())
                    .nature("Serene")
                    .name(pokemon.getName())
                    .originalTrainer(userDB.getId())
                    .status("PC")
                    .build();
            userPokemonRepository.save(userPokemon);

            return "Congratulations!! You have caught " + name + ", its id is: " + userPokemon.getId();
        }
        return "Oh no, " + name  +" has escaped";
    }
    /**
     * @author Pedro Aldama

     * @return List muestra una lista con los detalles de los pokemon atrapados del usuario
     * no importa su status
     */
    @Override
    @Transactional(readOnly = true)
    public List<DTOPokemonUserResponse> listAllUserPokemon() {
        List<UserPokemon> pokemonList = userPokemonRepository.findAllByUser(getUserByUsername(getUser()));
        return listUserPokemonToDTOPokemonUserResponse(pokemonList);
    }

    /**
     * @author Pedro Aldama
     * @param status status por el que se buscan
     * @return List muestra una lista con los detalles de los pokemon atrapados del usuario
     *  filtrando su status
     */
    @Override
    @Transactional(readOnly = true)
    public List<DTOPokemonUserResponse> listAllUserPokemonByStatus( String status) {
        List<UserPokemon> pokemonList = userPokemonRepository.findByUserAndStatus(getUserByUsername(getUser()),status);
        return listUserPokemonToDTOPokemonUserResponse(pokemonList);
    }

    /**
     * @author Pedro Aldama
     * @param id id de userPokemon
     * @return DTOPokeonUserResponse muestra los detalles del pokeon con ese id de captura
     */
    @Override
    @Transactional(readOnly = true)
    public DTOPokemonUserResponse getUserPokemonById( int id) {
            UserPokemon userPokemon = getUserPokemon(getUser(), id);
        return userPokemonToDTOPokemonUserResponse(userPokemon);
    }

    /**
     * @author Pedro Aldama
     * @param id id del pokemon
     * @param item nombre del item que se le dara al pokemon
     * @return String dependiendo del tipo de item tendrá un comportamiento diferente:
     * si es medicinal curará al pokemon, si es evolutivo se lo equipara, si el pokemon
     * ya tiene uno, este item será intercambiado y regresará a la mochila
     */
    @Override
    @Transactional
    public String giveItemToUserPokemon( int id, String item) {
        UserPokemon pokemon = getUserPokemon(getUser(), id);
        String type = verifyItemType(item);
        if(bagService.removeItemFromBag(type,item)){
            if(type.equalsIgnoreCase("Consumable")){
                long life = pokemon.getCurrentLife();
                long newLife = life + 30;//Actualmente todas las berry o consumable curan solo 30
                pokemon.setCurrentLife(Math.min(newLife,pokemon.getMaxLife()));
                userPokemonRepository.save(pokemon);
                return item + " has been given to " + pokemon.getName() + " and current life is " + pokemon.getCurrentLife();
            }
            if(!pokemon.getItem().isEmpty()) {
                bagService.addItemToBag(verifyItemType(pokemon.getItem()),pokemon.getItem());
            }
            pokemon.setItem(item);
            userPokemonRepository.save(pokemon);
            return item + " has been given to " + pokemon.getName();
        }
        return "You don't have " + item + " in this bag";
    }

    /**
     * @author Pedro Aldama
     * @param id  id del pokemon
     * @return String extrae el item del pokemon y lo almacena en la mochila si tenia uno
     */
    @Override
    @Transactional
    public String getItemFromUserPokemon( int id) {
        UserPokemon pokemon = getUserPokemon(getUser(), id);
        String takenItem = pokemon.getItem();
        String response = bagService.addItemToBag(verifyItemType(pokemon.getItem()),pokemon.getItem());
        pokemon.setItem("");
        userPokemonRepository.save(pokemon);
        return response + "  " + takenItem + " has been taken from " + pokemon.getName() + " and now it's in your bag";
    }
    /**
     * @author Pedro Aldama
     * @param newName nuevo nombre del pokemon
     * @param idPokemon id del userPokemon que se quiere cambiar
     * @return String muestra un mensaje si se pudo cambiar el nombre o no
     */
    @Override
    @Transactional
    public String changePokemonName(String newName, long idPokemon) {
        UserPokemon pokemon = getUserPokemon(getUser(),(int) idPokemon);
        String oldName = pokemon.getName();
        pokemon.setName(newName);
        userPokemonRepository.save(pokemon);
        return "Congratulations!, " + oldName + " has been changed it's name to " + pokemon.getName();
    }

    /**
     * @author Pedro Aldama
     * @param newStatus nuevo status del pokemon
     * @param idPokemon id del userPokemon que se quiere cambiar
     * función que sirve para cambiar de estado a un pokemon, puede ser PC,DC,Team o en intercambio
     */
    @Override
    @Transactional
    public void changeStatus( String newStatus, long idPokemon) {
        UserPokemon pokemon = getUserPokemon(getUser(), (int) idPokemon);
        pokemon.setStatus(newStatus);
        userPokemonRepository.save(pokemon);
    }
    /**
     * @author Pedro Aldama
     * @param idPokemon id del userPokemon que se quiere depositar
     * @return String muestra un mensaje de que el pokemon está en DayCare, su status cambia a DC
     */
    @Override
    @Transactional
    public String setPokemonInDayCare(long idPokemon) {
        if(userPokemonRepository.findStatusById(idPokemon).equals("PC")){
            userPokemonRepository.setStatus("DC",idPokemon);
            return redisService.addPokemonToDayCare(getUser(),idPokemon);
        }
        return "Your Pokemon is not available to DayCare";
    }
    /**
     * @author Pedro Aldama
     * @param idPokemon id del userPokemon que se quiere obtener
     * @return String muestra un mensaje de cuando el pokemon es recolectado
     * se calcula la nueva experiencia de acuerdo al tiempo pasado en DC
     * si excede su experiencia maxima, este puede evolucionar si tiene ese trigger
     * Se usa PokeApi para buscar el trigger y el nombre y id de la siguiente evolution
     */
    @Override
    @Transactional
    public String getPokemonFromDayCare(long idPokemon) {
        String response = redisService.removePokemonFromRoom(getUser());
        String message = "Here is your pokemon!!";
        if(response.split(":").length < 2){
            return response;
        }
        int experience =Integer.parseInt(response.split(":")[1]) * 30;
        UserPokemon pokemon = getUserPokemon(getUser(), (int) idPokemon);
        pokemon.setCurrentExperience(experience + pokemon.getCurrentExperience());
        if(pokemon.getMaxExperience() < pokemon.getCurrentExperience()){
            String evolutionTrigger = pokeAPIService.getEvolutionTrigger(idPokemon +"");
            if(evolutionTrigger.contains("Level")){
                String pokemonName = evolutionTrigger.split("_")[1];
                pokemon.setIdPokemon(pokeAPIService.getPokemonId(pokemonName));
                pokemon.setName(pokemonName);
                message+=", Congratulation, now you have a " + pokemonName;
            }
                pokemon.setCurrentExperience(pokemon.getMaxExperience());
        }
        userPokemonRepository.setStatus("PC",idPokemon);
        return message;
    }

    /**
     * @author Pedro Aldama
     * @param username usuario con quien intercambiar
     * @param idPokemon id del userPokemon que se quiere cambiar
     * @return String muestra un mensaje de que el pokemon se ha intercambiado
     * el userPokemon->id se intercambia, cambiando de propietario del pokemon
     * se usa redis para almacenar Id de cada pokemon a intercambiar
     */
    @Override
    @Transactional
    public String changePokemonWithOther(String username, long idPokemon) {
        UserPokemon pokemon = getUserPokemon(getUser(),idPokemon);
        if(!pokemon.getStatus().equals("PC")){
            return "Your Pokemon is not available to Swap";
        }
        long idPokemonUser = redisService.joinToRoomToSwap(username);
        if(idPokemonUser == -1 || idPokemonUser == 0){
            return "Nothing to change";
        }
        UserPokemon pokemonToSwap = getUserPokemon(username,idPokemonUser);
        pokemonToSwap.setUser(usersService.getUserByUsername(getUser()));
        pokemonToSwap.setStatus("PC");
        pokemon.setUser(usersService.getUserByUsername(username));
        userPokemonRepository.save(pokemonToSwap);
        userPokemonRepository.save(pokemon);
        redisService.deleteRoomFromSwap(username);
        return "Your Pokemon has been changed to " + pokemonToSwap.getName();
    }
    /**
     * @author Pedro Aldama
     * @param idPokemon id del userPokemon que se quiere cambiar
     * @return String muestra un mensaje de que el pokemon está listo para intercambio
     * se crea una sala en redis para que otro entrenador pueda consultarla e intercambiar
     */
    @Override
    @Transactional
    public String createRoomToChange(long idPokemon) {
        UserPokemon pokemon = getUserPokemon(getUser(),idPokemon);
        if(!pokemon.getStatus().equals("PC")){
            return "Your Pokemon is not available to Swap";
        }
        redisService.createRoomToSwap(getUser(),idPokemon);
        pokemon.setStatus("SP");
        userPokemonRepository.save(pokemon);
        return "Your Pokemon added to Room";
    }


    private Users getUserByUsername(String username) {
        return usersService.getUserByUsername(username);
    }
    private UserPokemon getUserPokemon(String user, long id) {
        return userPokemonRepository.findById(id).orElseThrow(PokemonException::new);
    }
    private String getUser(){
        return getAuthenticatedUsername();
    }
}
