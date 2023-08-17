package com.kruk.deliveryservice.consumer;

import com.kruk.deliveryservice.dto.InventoryKafkaDto;

public interface KafkaConsumer {
    public void consumeInventory(InventoryKafkaDto inventoryKafkaDto);

}
