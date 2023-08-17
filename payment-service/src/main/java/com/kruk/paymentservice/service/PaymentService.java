package com.kruk.paymentservice.service;

import com.kruk.paymentservice.domain.UserBalance;
import com.kruk.paymentservice.dto.OrderKafkaDto;
import com.kruk.paymentservice.dto.UserKafkaDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PaymentService {

    Optional<UserBalance> topUpBalance(Long amount, String userName);
    public String retrieveUserNameFromToken(String token);

    public void saveToUserBalance(UserKafkaDto userKafkaDto);
    public void processOrderMessage(OrderKafkaDto orderKafkaDto);

    public void processFallbackMessage(OrderKafkaDto orderKafkaDto);
}
