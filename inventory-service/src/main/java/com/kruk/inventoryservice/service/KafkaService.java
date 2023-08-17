package com.kruk.inventoryservice.service;

import com.kruk.inventoryservice.dto.InventoryKafkaDto;
import com.kruk.inventoryservice.dto.PaymentKafkaDto;

public interface KafkaService {

    void produce(InventoryKafkaDto inventoryKafkaDto);

    void fallback(PaymentKafkaDto paymentKafkaDto);
}
