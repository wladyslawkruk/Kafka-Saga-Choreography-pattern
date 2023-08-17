package com.kruk.authservice.controller;

import com.kruk.authservice.repository.UserRepository;
import com.kruk.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.kruk.authservice.dto.UserDto;
import com.kruk.authservice.domain.User;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder,UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Operation(summary = "Get user by name", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        return userService.getUser(username).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @Operation(summary = "Get all users", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/")
    public ResponseEntity<List<User>> getAllUsers() {
       return ResponseEntity.ok(userService.getAllUsers());

    }

    @Operation(summary = "Delete user by name", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping (value = "/delete/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Create new user")
    @PostMapping(value = "/signup")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDto userDto) {
        return userService.addUser(userDto)
                .map(order -> ResponseEntity.status(HttpStatus.CREATED).body(order))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build());

    }
}

