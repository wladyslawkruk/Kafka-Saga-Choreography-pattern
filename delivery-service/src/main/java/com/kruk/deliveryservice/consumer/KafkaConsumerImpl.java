package com.kruk.deliveryservice.consumer;

import com.kruk.deliveryservice.dto.InventoryKafkaDto;
import com.kruk.deliveryservice.service.DeliveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KafkaConsumerImpl implements KafkaConsumer{

    private final DeliveryService deliveryService;
    private final Logger logger = LoggerFactory.getLogger(KafkaConsumerImpl.class);

    @Autowired
    public KafkaConsumerImpl(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Override
    @Transactional
    @KafkaListener(topics = "inventory", groupId = "inventoryGroupId",
            properties = {"spring.json.value.default.type=com.kruk.deliveryservice.dto.InventoryKafkaDto"})
    public void consumeInventory(InventoryKafkaDto inventoryKafkaDto) {
        deliveryService.processInventoryMessage(inventoryKafkaDto);

    }
}
