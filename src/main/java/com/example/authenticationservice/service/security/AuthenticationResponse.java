package com.example.authenticationservice.service.security;

import lombok.*;

@Builder
@Getter
@Setter
public class AuthenticationResponse {
    private String jwtToken;

}
