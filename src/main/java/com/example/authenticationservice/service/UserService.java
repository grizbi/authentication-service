package com.example.authenticationservice.service;

import com.example.entitiesservice.repository.User;

public interface UserService {
    /**
     * Throws InvalidActivityException if user already exists.
     *
     * @param user User to be added
     */
    Void addUser(User user);
}
