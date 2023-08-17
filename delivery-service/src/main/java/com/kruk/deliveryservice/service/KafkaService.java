package com.kruk.deliveryservice.service;

import com.kruk.deliveryservice.dto.InventoryKafkaDto;

public interface KafkaService {
    void produce(InventoryKafkaDto inventoryKafkaDto);
}
