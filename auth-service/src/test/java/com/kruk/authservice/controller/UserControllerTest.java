package com.kruk.authservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.kruk.authservice.domain.User;
import com.kruk.authservice.repository.UserRepository;
import com.kruk.authservice.security.SecurityConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
@WithMockUser(username = "Kowalski")
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Configuration
    @ComponentScan(basePackageClasses = {UserController.class, SecurityConfiguration.class})
    public static class TestConf {
    }

    private User user;

    private User newUser;

    private List<User> users;

    @BeforeEach
    public void setUp() {
        Mockito.when(passwordEncoder.encode(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0) + "_some_fake_encoding");
        user = new User(
                "Bosak",
                passwordEncoder.encode("superpass")
        );
        newUser = new User(
                "Szydlo",
                passwordEncoder.encode("superpass99")
        );
        users = Collections.singletonList(user);
    }

    @Test
    public void getUser() throws Exception {
        Mockito.when(userRepository.findByName(user.getName())).thenReturn(Optional.of(user));
        mvc.perform(get("/user/Szydlo"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(user.getName())));
    }

    @Test
    public void getAllUsers() throws Exception {
        Mockito.when(userRepository.findAll()).thenReturn(users);
        mvc.perform(get("/user/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(user.getName())));
    }

    @Test
    public void createUser() throws Exception {
        Mockito.when(userRepository.save(refEq(newUser))).thenReturn(newUser);
        mvc.perform(
                post("/user/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Szydlo\",\"password\":\"superpass99\"}")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(newUser.getName())));
    }
}
