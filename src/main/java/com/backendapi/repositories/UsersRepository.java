package com.backendapi.repositories;

import com.backendapi.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    @Query("Select u from Users u where u.name = :name")
    Optional<Users> findByName(String name);
    Boolean existsByName(String username);
    Boolean existsByEmail(String email);
}
