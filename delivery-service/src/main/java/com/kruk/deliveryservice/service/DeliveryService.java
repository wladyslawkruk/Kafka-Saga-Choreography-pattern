package com.kruk.deliveryservice.service;

import com.kruk.deliveryservice.dto.InventoryKafkaDto;

public interface DeliveryService {
    public void processInventoryMessage(InventoryKafkaDto inventoryKafkaDto);
}
