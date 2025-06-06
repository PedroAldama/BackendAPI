package com.backendapi.services.users;

import com.backendapi.entities.Users;

public interface UsersService {
    Users getUserByUsername(String username);
}
