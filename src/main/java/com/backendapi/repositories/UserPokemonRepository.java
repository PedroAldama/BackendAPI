package com.backendapi.repositories;

import com.backendapi.entities.UserPokemon;
import com.backendapi.entities.Users;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPokemonRepository extends JpaRepository<UserPokemon, Long> {
    List<UserPokemon> findAllByUser(Users user);
    List<UserPokemon> findByUserAndStatus(Users user, String status);
    Optional<UserPokemon> findByUserAndIdPokemon(Users userName, long idPokemon);
    @Query("Select u.status from UserPokemon u where u.id = :id")
    String findStatusById(long id);
    @Modifying
    @Query("UPDATE UserPokemon u SET u.status = :newStatus WHERE u.id = :id")
    void setStatus(String newStatus,long id);
}
