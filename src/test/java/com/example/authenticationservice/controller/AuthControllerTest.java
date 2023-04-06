package com.example.authenticationservice.controller;

import com.example.authenticationservice.service.security.AuthenticationRequest;
import com.example.entitiesservice.repository.User;
import com.example.entitiesservice.repository.UserRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestPropertySource(locations = "classpath:application.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthControllerTest {

    private static final String USERNAME = "test";
    private static final String PASSWORD = "123";
    private static final String USER_URI = "/users";
    private static final String LOGIN_URI = "/login";
    @LocalServerPort
    private int port;
    @Autowired
    private UserRepository userRepository;

    private String createURI(String uri) {
        return "http://localhost:" + port + uri;
    }

    @BeforeEach
    void cleanUpDb() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUserWhenUsernameIsNotTaken() {
        String uri = createURI(USER_URI);

        given().contentType(ContentType.JSON).body(getUser()).when().post(uri).then().statusCode(200);
        assertNotNull(userRepository.findByUsername(USERNAME));
    }

    @Test
    void shouldNotCreateUserWhenUsernameIsTaken() {
        userRepository.save(getUser());

        String uri = createURI(USER_URI);

        given().contentType(ContentType.JSON).body(getUser()).when().post(uri).then().statusCode(400);
        assertEquals(1, userRepository.findAll().size());
    }

    private User getUser() {
        return new User(1L, PASSWORD, USERNAME, 5000);
    }

    @Test
    void shouldGetTokenWhenCredentialsValid() {
        userRepository.save(getUser());

        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .password(PASSWORD)
                .username(USERNAME)
                .build();

        String uri = createURI(LOGIN_URI);

        given().contentType(ContentType.JSON).body(authenticationRequest).when().post(uri).then()
                .body("jwtToken", is(not(emptyString()))).statusCode(200);
    }

    @Test
    void shouldRespond403WhenCredentialsInvalid() {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .password("daSOD")
                .username("DUMMY")
                .build();

        String uri = createURI(LOGIN_URI);

        given().contentType(ContentType.JSON).body(authenticationRequest).when().post(uri).then()
                .statusCode(403).extract().asString();

        assertEquals(0, userRepository.findAll().size());
    }
}