package com.backendapi.services.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService{
    private final StringRedisTemplate stringRedisTemplate;
    private static final String ROOM = "room:";
    private static final String QUESTION = "question:";
    private static final String DAYCARE = "daycare:";

    @Override
    @Transactional
    public void createRoomToSwap(String user, long idUserPokemon) {
        stringRedisTemplate.opsForValue().set(ROOM + user, String.valueOf(idUserPokemon));
    }

    @Override
    @Transactional
    public long joinToRoomToSwap(String roomName) {
        String idPokemon = stringRedisTemplate.opsForValue().get(ROOM + roomName);
        if(idPokemon == null) { return 0; }
        return Long.parseLong(idPokemon);
    }

    @Override
    public void deleteRoomFromSwap(String roomName) {
        stringRedisTemplate.delete(ROOM+roomName);
    }

    @Override
    @Transactional
    public String addPokemonToDayCare(String user, long idPokemonUser) {
        String storedData = stringRedisTemplate.opsForValue().get(DAYCARE + user);
        if(storedData != null) return "You have already a Pokemon in DayCare";

        LocalDateTime now = LocalDateTime.now();
        String formattedDate = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String data =  idPokemonUser + ":" + formattedDate;
        stringRedisTemplate.opsForValue().set(DAYCARE+user,data);
        return "";
    }

    @Override
    @Transactional
    public String removePokemonFromRoom(String user) {
        String storedData = stringRedisTemplate.opsForValue().get(DAYCARE + user);
        if(storedData == null) return "You don't have this Pokemon in Day Care";

        String[] parts = storedData.split(":");

        long idPokemon = Long.parseLong(parts[0]);
        LocalDateTime dateStored = LocalDateTime.parse(parts[1]);

        // Calcular el tiempo que paso
        Duration duration = Duration.between(dateStored, LocalDateTime.now());
        long days = duration.toDays();
        stringRedisTemplate.delete(DAYCARE+user);
        return idPokemon + ":" + days;
    }

    @Override
    @Transactional
    public void addQuestionForUser(String user, String question, String answer) {
        stringRedisTemplate.opsForValue().set(QUESTION + user, question + ":" + answer);
    }

    @Override
    @Transactional
    public void removeQuestionFromUser(String user) {
        stringRedisTemplate.delete(QUESTION+user);
    }

    @Override
    public String getAnswerFromUser(String user) {
        String storedData = stringRedisTemplate.opsForValue().get(QUESTION + user);
        if(storedData == null) return "You don't have any question yet";
        return storedData;
    }

}
