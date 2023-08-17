package com.kruk.authservice.service;

import com.kruk.authservice.dto.UserKafkaDto;

public interface KafkaService {
    void produce(UserKafkaDto userKafkaDto);
}
