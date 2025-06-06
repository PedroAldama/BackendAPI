package com.backendapi.services.redis;

public interface RedisService {
    String createRoomToSwap(String user,long idUserPokemon);
    long joinToRoomToSwap(String roomName);
    String addPokemonToDayCare(String user,long idPokemonUser);
    String removePokemonFromRoom(String user , long idPokemonUser);
    void addQuestionForUser(String user, String question, String answer);
    void removeQuestionFromUser(String user);
    String getAnswerFromUser(String user);
}
