package com.backendapi.repositories;

import com.backendapi.entities.UserPokemon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPokemonRepository extends JpaRepository<UserPokemon, Long> {
}
