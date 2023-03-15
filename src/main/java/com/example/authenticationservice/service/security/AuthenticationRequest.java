package com.example.authenticationservice.service.security;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AuthenticationRequest {

    private static final int MINIMUM_CREDENTIALS_SIZE = 3;
    private static final int MAXIMUM_CREDENTIALS_SIZE = 20;

    @NotNull
    @Size(min = MINIMUM_CREDENTIALS_SIZE, max = MAXIMUM_CREDENTIALS_SIZE)
    private String username;
    @NotNull
    @Size(min = MINIMUM_CREDENTIALS_SIZE, max = MAXIMUM_CREDENTIALS_SIZE)
    private String password;

}
