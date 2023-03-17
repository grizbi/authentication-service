package com.example.authenticationservice.controller;

import com.example.authenticationservice.service.security.AuthenticationRequest;
import com.example.authenticationservice.service.security.AuthenticationResponse;
import com.example.authenticationservice.service.security.AuthenticationService;
import com.example.authenticationservice.service.security.UserService;
import com.example.entitiesservice.repository.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    public void addUser(@Valid @RequestBody User user) {
        userService.addUser(user);
    }
}
