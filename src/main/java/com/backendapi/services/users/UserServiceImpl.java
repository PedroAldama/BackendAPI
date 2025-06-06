package com.backendapi.services.users;

import com.backendapi.entities.Users;
import com.backendapi.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UsersService{
    private final UsersRepository usersRepository;

    @Override
    public Users getUserByUsername(String username) {
        return usersRepository.findByName(username).orElseThrow();
    }
}
