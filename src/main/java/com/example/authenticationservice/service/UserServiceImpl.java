package com.example.authenticationservice.service;

import com.example.authenticationservice.exception.InvalidActivityException;
import com.example.authenticationservice.service.security.CustomUserDetails;
import com.example.entitiesservice.repository.User;
import com.example.entitiesservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return new CustomUserDetails(user);
    }

    public Void addUser(User user) {
        if (isUsernameDuplicated(user.getUsername())) {
            throw new InvalidActivityException("Specified username already exists in DB.");
        }

        userRepository.save(user);
        return null;
    }

    private boolean isUsernameDuplicated(String username) {
        Optional<User> foundUser = userRepository.findByUsername(username);

        return foundUser.isPresent();
    }
}
