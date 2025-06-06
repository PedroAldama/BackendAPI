package com.backendapi.entities.users;

import com.backendapi.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializerRoles implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        for (ERole erole : ERole.values()) {
            if (!roleRepository.existsByName(erole)) {
                roleRepository.save(new Role(null, erole));
            }
        }
    }
}
