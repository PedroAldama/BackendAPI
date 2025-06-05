package com.backendapi.services.users;

import com.backendapi.entities.Users;

public interface UsersService {
    Users registerUser(Users user);
    String loginUser(Users user);
    Users getUserByUsername(String username);
}
