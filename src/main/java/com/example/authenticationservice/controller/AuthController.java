package com.example.authenticationservice.controller;

import com.example.authenticationservice.exception.InvalidActivityException;
import com.example.authenticationservice.service.UserService;
import com.example.authenticationservice.service.security.AuthenticationRequest;
import com.example.authenticationservice.service.security.AuthenticationResponse;
import com.example.authenticationservice.service.security.AuthenticationService;
import com.example.entitiesservice.repository.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

    @PostMapping("/users")
    public ResponseEntity<Void> addUser(@Valid @RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.addUser(user));
        } catch (InvalidActivityException exception) {
            log.error("User already exists in DB.", exception);
            return ResponseEntity.badRequest().build();
        }
    }
}
