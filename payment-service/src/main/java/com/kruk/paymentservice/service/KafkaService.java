package com.kruk.paymentservice.service;

import com.kruk.paymentservice.dto.OrderKafkaDto;
import com.kruk.paymentservice.dto.PaymentKafkaDto;

public interface KafkaService {
    void produce(PaymentKafkaDto paymentKafkaDto);

    void fallback(OrderKafkaDto orderKafkaDto);
}
