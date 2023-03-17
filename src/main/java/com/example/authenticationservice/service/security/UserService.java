package com.example.authenticationservice.service.security;

import com.example.authenticationservice.exception.InvalidActivityException;
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
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        return user.map(CustomUserDetails::new).orElseGet(() -> CustomUserDetails.builder()
                .user(new User())
                .build());

    }

    public void addUser(User user) {
        if (isUsernameDuplicated(user.getUsername())) {
            log.error("Username already exists in DB");
            throw new InvalidActivityException("Specified username already exists in DB.");
        }
        userRepository.save(user);
    }

    private boolean isUsernameDuplicated(String username) {
        Optional<User> foundUser = userRepository.findByUsername(username);

        return foundUser.isPresent();
    }
}
