package com.backendapi.services.pokeapi;

import com.backendapi.dto.pokeapi.*;
import com.backendapi.dto.pokeapi.evolution.Chain;
import com.backendapi.dto.pokeapi.evolution.EvolutionDetail;
import com.backendapi.dto.pokeapi.evolution.EvolutionTriggerResponse;
import com.backendapi.dto.pokeapi.evolution.EvolvesTo;
import com.backendapi.dto.pokeapi.pokemon.PokemonResponse;
import com.backendapi.exceptions.PokeApiException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;
/**
 * @author Pedro Aldama
 * Clase principal de la llamada a la PokeApi, esta clase se inyecta en todas las demas para que
 * esta sea la unica que tenga que llamar a la PokeApi
 */
@Service
@RequiredArgsConstructor
public class PokeAPIServiceImpl implements PokeAPIService{
    //Inyecto el webclient que cree en configuration mediante constructor con la anotacion requiredArgsConstructor
    private final WebClient webClient;

    /**
     * @author Pedro Aldama
     * @param name El nombre o Id del pokemon a buscar
     * @return PokemonResponse Es el DTO que tiene solo los datos necesarios para nuestra app
     * como el id, nombre, experiencia base, tipos y stats, ademas de cosas extras
     */
    @Override
    public PokemonResponse getPokemonByName(String name) {
        return webClient.get()
                .uri("/pokemon/{name}", name)
                .retrieve()
                .onStatus(
                        status -> status.value() == 404,
                        clientResponse -> Mono.error(new PokeApiException("Pokemon not found"))
                )
                .bodyToMono(PokemonResponse.class)
                .block();
    }

    /**
     * @author Pedro Aldama
     * @param id  Id del pokemon a buscar
     * @return PokemonResponse Es el DTO que tiene solo los datos necesarios para nuestra app
     * como el id, nombre, experiencia base, tipos y stats, ademas de cosas extras
     */
    @Override
    public PokemonResponse getPokemonById(int id) {
        return webClient.get()
                .uri("/pokemon/{id}", id)
                .retrieve()
                .bodyToMono(PokemonResponse.class)
                .block();
    }

    /**
     * @author Pedro Aldama
     * @param name El nombre pokemon a buscar
     * @return int retorna el id del pokemon
     */
    @Override
    public int getPokemonId(String name) {
         return Objects.requireNonNull(webClient.get()
                        .uri("/pokemon/{name}", name)
                        .retrieve()
                        .bodyToMono(PokemonResponse.class)
                        .block())
                .getId();
    }

    /**
     * @author Pedro Aldama
     * @param name El nombre o Id del pokemon a buscar
     * @return int retorna la probabilidad de captura del pokemon min 1 max 255
     */
    @Override
    public int getCaughtProbability(String name) {
             String stringValue =   webClient.get()
                .uri("/pokemon-species/{name}", name)
                .retrieve()
                     .onStatus(
                             status -> status.value() == 404,
                             clientResponse -> Mono.error(new PokeApiException("Pokemon not found"))
                     )
                .bodyToMono(JsonNode.class)
                .map(jsonNode -> jsonNode.path("capture_rate").asText())
                .block();
             if(stringValue == null){
                 return 0;
             }
             return Integer.parseInt(stringValue);
    }
    /**
     * @author Pedro Aldama
     * @param name El nombre o Id del pokemon a buscar
     * @return String una combinacion del trigger de evolucion asi como el nivel
     * o el objeto que la provoca
     */
    @Override
    public String getEvolutionTrigger(String name) {

        String evolutionUri = webClient.get().uri("/pokemon-species/{name}",name).retrieve()
                .onStatus(
                        status -> status.value() == 404,
                        clientResponse -> Mono.error(new PokeApiException("Pokemon not found"))
                )
                .bodyToMono(JsonNode.class)
                .map(jn -> jn.path("evolution_chain").get("url").asText())
                .block();
        if(evolutionUri == null){
            return "Pokemon not found";
        }
        evolutionUri = evolutionUri.split("v2/")[1];

        return webClient.get().uri(evolutionUri).retrieve().bodyToMono(EvolutionTriggerResponse.class)
                .map(js -> getEvolutionMethod(js,name))
                .block();
    }

    /**
     * @author Pedro Aldama
     * @return Set<String> Retorna todos los item que sean de la categoria medicine
     */
    @Override
    public Set<String> getAllConsumableItems() {
        return getAllItemByCategory("medicine");
    }
    /**
     * @author Pedro Aldama
     * @return Set<String> Retorna todos los item que sean de la categoria evolution
     */
    @Override
    public Set<String> getAllEvolutionItems() {
        return getAllItemByCategory("evolution");
    }

    /**
     * @author Pedro Aldama
     * @param generation el nombre o numero de la generacion a buscar
     * @return Map retorna el Id junto al nombre del pokemon de la generacion en forma de lista
     */
    @Override
    public Map<Integer, String> getAllPokemonIdAndNameByGeneration(String generation) {
        Map<Integer, String> pokemonIdAndName = new HashMap<>();
        JsonNode response =  webClient.get()
                .uri("/generation/{generation}",generation)
                .retrieve()
                .onStatus(
                        status -> status.value() == 404,
                        clientResponse -> Mono.error(new PokeApiException("Generation not found"))
                )
                .bodyToMono(JsonNode.class)
                .block();
        if(response != null){
            for(JsonNode pokemon: response.path("pokemon_species")){
                pokemonIdAndName.put(extractIdFromUrl(pokemon.path("url").asText()),pokemon.path("name").asText());
            }
        }

        return pokemonIdAndName;
    }
    /**
     * @author Pedro Aldama
     * @param type el tipo del pokemon que se busca
     * @return TypesResponse un DTO que incluye una lista de pokemon y sus tipos
     */
    @Override
    public TypesResponse getAllPokemonByType(String type) {
        return webClient.get()
                .uri("/type/{type}",type)
                .retrieve()
                .bodyToMono(TypesResponse.class)
                .block();
    }
    /**
     * @author Pedro Aldama
     * @return String busca un pokemon al azar entre todos los disponibles
     */
    @Override
    public String getRandomPokemon() {
        return webClient.get()
                .uri("/pokemon/"+ (int)(Math.random() * 1025))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(jn -> jn.get("name").asText())
                .block();
    }

    /**
     * @author Pedro Aldama
     * @param generation el nombre o numero de la generacion a buscar
     * @return Mono<List> retorna nombre de los pokemon de la generacion en forma de lista Mono
     * lo realice de esta manera solo para entender el funcionamiento de flux y mono
     */
    @Override
    public Mono<List<String>> getAllPokemonByGeneration(String generation) {
        return webClient.get()
                .uri("/generation/{gen}", generation)
                .retrieve()
                .onStatus(
                        status -> status.value() == 404,
                        clientResponse -> Mono.error(new PokeApiException("Generation not found"))
                )
                .bodyToMono(GenerationResponse.class)
                .flatMapMany(response -> Flux.fromIterable(response.getPokemonSpecies()))
                .map(GenerationResponse.PokemonSpecies::getName)
                .collectList();
    }

    //Funcion que sirve para extraer el id del pokemon de la url
    private int extractIdFromUrl(String url){
        return Integer.parseInt(url.split("pokemon-species")[1].replace("/", ""));
    }

    //Funcion para evitar repetir el obtener los item categorias
    private Set<String> getAllItemByCategory(String category){
        ItemsResponse items =  webClient.get()
                .uri("/item-category/{category}",category)
                .retrieve()
                .bodyToMono(ItemsResponse.class)
                .block();
        if(items == null){
            return Set.of();
        }
        return items.getItems().stream().map(ItemsResponse.Items::getName).collect(Collectors.toSet());
    }

    //Funcion para evitar que la principal sea muy grande, busca a travez del Chain si el pokemon
    //es el buscado en caso de que no se llama a otra funcion
    private String getEvolutionMethod(EvolutionTriggerResponse response, String target){
        Chain chain = response.getChain();
        List<EvolvesTo> pokemonEvolution;
        if(!chain.getSpecies().getName().equals(target)){
            pokemonEvolution = findPokemonEvolvesTo(chain,target);
        }else{
            pokemonEvolution = chain.getEvolvesTo();
        }

        if(pokemonEvolution == null || pokemonEvolution.isEmpty()){
            return "This Pokemon can't evolve";
        }

        EvolutionDetail details = pokemonEvolution.getFirst().getEvolutionDetails().getFirst();
        String trigger = details.getTrigger().getName();
        String pokemonToEvolution = pokemonEvolution.getFirst().getSpecies().getName();

        return triggerEvolution(details,trigger,pokemonToEvolution);
    }

    //Funcion que itera hasta encontrar el pokemon que se desea para ver su EvolutionDetails
    private List<EvolvesTo> findPokemonEvolvesTo(Chain chain, String target){
        List<EvolvesTo> response = chain.getEvolvesTo();
        for(EvolvesTo pokemon: response){
            if(pokemon.getSpecies().getName().equals(target)){
                return pokemon.getEvolvesTo();
            }
        }
        return List.of();
    }
    //Funcion de respuesta de trigger de evolution
    private String triggerEvolution(EvolutionDetail details, String trigger, String pokemonToEvolution){


        if ("level-up".equals(trigger)) {
            return  trigger + ", Level : " + details.getMinLevel() + "_" + pokemonToEvolution;
        } else if ("use-item".equals(trigger) && details.getItem() != null) {
            return trigger + ", Item : " + details.getItem().getName() + "_" + pokemonToEvolution;
        } else {
            return trigger;
        }
    }

}
