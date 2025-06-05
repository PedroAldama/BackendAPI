package com.backendapi.services.pokeapi;

import com.backendapi.dto.pokeapi.*;
import com.backendapi.dto.pokeapi.evolution.Chain;
import com.backendapi.dto.pokeapi.evolution.EvolutionDetail;
import com.backendapi.dto.pokeapi.evolution.EvolutionTriggerResponse;
import com.backendapi.dto.pokeapi.evolution.EvolvesTo;
import com.backendapi.dto.pokeapi.pokemon.PokemonResponse;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

//Importo de forma static para usar solo el nombre de las funciones
@Service
@RequiredArgsConstructor
public class PokeAPIServiceImpl implements PokeAPIService{
    //Inyecto el webclient que cree en configuration mediante constructor con la anotacion requiredArgsConstructor
    private final WebClient webClient;

    @Override
    public PokemonResponse getPokemonByName(String name) {
        return webClient.get()
                .uri("/pokemon/{name}", name)
                .retrieve()
                .bodyToMono(PokemonResponse.class)
                .block();
    }

    @Override
    public PokemonResponse getPokemonById(int id) {
        return webClient.get()
                .uri("/pokemon/{id}", id)
                .retrieve()
                .bodyToMono(PokemonResponse.class)
                .block();
    }

    @Override
    public int getPokemonId(String name) {
         return Objects.requireNonNull(webClient.get()
                        .uri("/pokemon/{name}", name)
                        .retrieve()
                        .bodyToMono(PokemonResponse.class)
                        .block())
                .getId();
    }

    @Override
    public int getCaughtProbability(String name) {
             String stringValue =   webClient.get()
                .uri("/pokemon-species/{name}", name)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(jsonNode -> jsonNode.path("capture_rate").asText())
                .block();
             if(stringValue == null){
                 return 0;
             }
             return Integer.parseInt(stringValue);
    }
    @Override
    public String getEvolutionTrigger(String name) {

        String evolutionUri = webClient.get().uri("/pokemon-species/{name}",name).retrieve()
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

    @Override
    public Set<String> getAllConsumableItems() {
        return getAllItemByCategory("medicine");
    }

    @Override
    public Set<String> getAllEvolutionItems() {
        return getAllItemByCategory("evolution");
    }

    @Override
    public Map<Integer, String> getAllPokemonIdAndNameByGeneration(String generation) {
        Map<Integer, String> pokemonIdAndName = new HashMap<>();
        JsonNode response =  webClient.get()
                .uri("/generation/{generation}",generation)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        if(response != null){
            for(JsonNode pokemon: response.path("pokemon_species")){
                pokemonIdAndName.put(extractIdFromUrl(pokemon.path("url").asText()),pokemon.path("name").asText());
            }
        }

        return pokemonIdAndName;
    }

    @Override
    public TypesResponse getAllPokemonByType(String type) {
        return webClient.get()
                .uri("/type/{type}",type)
                .retrieve()
                .bodyToMono(TypesResponse.class)
                .block();
    }

    @Override
    public Mono<List<String>> getAllPokemonByGeneration(String generation) {
        return webClient.get()
                .uri("/generation/{gen}", generation)
                .retrieve()
                .bodyToMono(GenerationResponse.class)
                .flatMapMany(response -> Flux.fromIterable(response.getPokemonSpecies()))
                .map(GenerationResponse.PokemonSpecies::getName)
                .collectList();
    }

    //Funcion que sirve para extraer el id del pokemon de la url
    private int extractIdFromUrl(String url){
        return Integer.parseInt(url.split("pokemon-species")[1].replace("/", ""));
    }

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

        return triggerEvolution(details,trigger);
    }

    private List<EvolvesTo> findPokemonEvolvesTo(Chain chain, String target){
        List<EvolvesTo> response = chain.getEvolvesTo();
        for(EvolvesTo pokemon: response){
            if(pokemon.getSpecies().getName().equals(target)){
                return pokemon.getEvolvesTo();
            }
        }
        return List.of();
    }
    private String triggerEvolution(EvolutionDetail details, String trigger){
        final String TRIGGER = "Trigger";

        if ("level-up".equals(trigger)) {
            return  TRIGGER + " " + trigger + ", Level: " + details.getMinLevel();
        } else if ("use-item".equals(trigger) && details.getItem() != null) {
            return TRIGGER + " " + trigger + ", Item: " + details.getItem().getName();
        } else {
            return TRIGGER + " " + trigger;
        }
    }

}
