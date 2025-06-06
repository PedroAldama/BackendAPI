package com.backendapi.services.question;

import com.backendapi.services.bag.BagService;
import com.backendapi.services.pokeapi.PokeAPIService;
import com.backendapi.services.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.backendapi.utils.SecurityUtils.getAuthenticatedUsername;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final RedisService redisService;
    private final PokeAPIService pokeAPIService;
    private final BagService bagService;
    /**
     * @author Pedro Aldama
     * @return String busca un nombre al azar de un pokemon y revuelve sus letras para retonarlo
     * lo guarda en redis para futuras consultas
     */
    @Override
    public String generateQuestion() {
        String pokemon = pokeAPIService.getRandomPokemon();
        String pokemonShuffle = shuffleName(pokemon);
        redisService.addQuestionForUser(getAuthenticatedUsername(),pokemonShuffle,pokemon);
        return "Guess the pokemon " + pokemonShuffle + ". Respond in /answer Endpoint with the param answer";
    }
    /**
     * @author Pedro Aldama
     * @param answer respuesta de usuario
     * @return String retorna si la respuesta es correcta o no consultando en redis
     */
    @Override
    public String answerQuestion(String answer) {
       String redisResponse = redisService.getAnswerFromUser(getAuthenticatedUsername());
       if(!redisResponse.contains(":")) {
           return redisResponse;
       }
       String originalAnswer = redisResponse.split(":")[1];
       String pokemonShuffle = redisResponse.split(":")[0];
       if(answer.equals(originalAnswer)) {
           bagService.setNewValance(100,"add");
           redisService.removeQuestionFromUser(getAuthenticatedUsername());
           return "Congratulations!!, you have the correct answer " +  originalAnswer +". You win 100 coins";
       }
        return "Keep Trying, remember the shuffle is " + pokemonShuffle;
    }

    /**
     * @author Pedro Aldama
     * @return String busca y retorna la respuesta en redis y borra la palabra en redis
     */
    @Override
    public String getAnswer() {
        String redisResponse = redisService.getAnswerFromUser(getAuthenticatedUsername());
        redisService.removeQuestionFromUser(getAuthenticatedUsername());
        return "The answer for " + redisResponse.split(":")[0] + " is " + redisResponse.split(":")[1];
    }

    //Funcion para revolver letras
    private String shuffleName(String pokemon) {
        List<Character> letters = new ArrayList<>();
        for (char c : pokemon.toCharArray()) {
            letters.add(c);
        }
        Collections.shuffle(letters);
        StringBuilder result = new StringBuilder();
        for (char c : letters) {
            result.append(c);
        }
        return result.toString();
    }

}
