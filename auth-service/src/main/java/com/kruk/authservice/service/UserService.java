package com.kruk.authservice.service;

import org.springframework.transaction.annotation.Transactional;
import com.kruk.authservice.domain.User;
import com.kruk.authservice.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> addUser(UserDto userDto);
    List<User> getAllUsers();
    @Transactional
    void deleteUser(String username);

    Optional<User> getUser(String username);

}
