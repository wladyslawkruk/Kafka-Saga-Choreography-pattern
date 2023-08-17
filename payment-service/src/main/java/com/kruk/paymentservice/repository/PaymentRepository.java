package com.kruk.paymentservice.repository;

import com.kruk.paymentservice.domain.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<UserBalance, Long> {
    Optional<UserBalance> findByName(String name);
}
