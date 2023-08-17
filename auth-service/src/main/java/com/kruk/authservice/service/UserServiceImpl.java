package com.kruk.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kruk.authservice.domain.User;
import com.kruk.authservice.dto.UserDto;
import com.kruk.authservice.dto.UserKafkaDto;
import com.kruk.authservice.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final KafkaService kafkaService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder,KafkaService kafkaService) {
        this.userRepository = userRepository;
        this.kafkaService = kafkaService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public Optional<User> addUser(UserDto userDto) {
        User saved = userRepository.save(new User(
                userDto.getName(),
                passwordEncoder.encode(userDto.getPassword())
       ));
        kafkaService.produce(UserKafkaDto.toKafkaDto(saved));
        return Optional.of(saved);


    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteUser(String username) {
        userRepository.deleteByName(username);

    }

    @Override
    public Optional<User> getUser(String username) {
        Optional<User> user = userRepository.findByName(username);
        return user;

    }


}
