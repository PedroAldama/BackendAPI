package com.backendapi.repositories;

import com.backendapi.entities.users.ERole;
import com.backendapi.entities.users.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
    boolean existsByName(ERole name);
}
