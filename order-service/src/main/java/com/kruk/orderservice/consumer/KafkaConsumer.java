package com.kruk.orderservice.consumer;

import com.kruk.orderservice.domain.OrderKafkaDto;

public interface KafkaConsumer {
    public void consumePayment(OrderKafkaDto orderKafkaDto);

}
