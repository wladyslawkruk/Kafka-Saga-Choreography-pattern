package com.kruk.orderservice.service;

import com.kruk.orderservice.domain.OrderKafkaDto;

public interface KafkaService {

    void produce(OrderKafkaDto orderKafkaDto);

}
