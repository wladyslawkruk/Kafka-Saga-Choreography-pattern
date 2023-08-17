package com.kruk.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kruk.authservice.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);

    void deleteByName(String name);
}
