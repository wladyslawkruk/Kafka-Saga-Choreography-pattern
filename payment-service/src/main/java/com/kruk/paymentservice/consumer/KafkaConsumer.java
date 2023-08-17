package com.kruk.paymentservice.consumer;

import com.kruk.paymentservice.dto.OrderKafkaDto;
import com.kruk.paymentservice.dto.PaymentKafkaDto;
import com.kruk.paymentservice.dto.UserKafkaDto;
import org.springframework.stereotype.Service;

@Service
public interface KafkaConsumer{
    public void consumeUser(UserKafkaDto userKafkaDto);
    public void consumeOrder(OrderKafkaDto orderKafkaDto);
    public void consumeInventory(PaymentKafkaDto paymentKafkaDto);
}
