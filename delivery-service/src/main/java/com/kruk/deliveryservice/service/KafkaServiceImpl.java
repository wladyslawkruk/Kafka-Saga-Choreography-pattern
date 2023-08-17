package com.kruk.deliveryservice.service;

import com.kruk.deliveryservice.dto.InventoryKafkaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceImpl implements KafkaService{

    private String fallback_topic = "fallback_to_inventory";

    private static final Logger logger = LoggerFactory.getLogger(KafkaServiceImpl.class);

    private final KafkaTemplate<Long, InventoryKafkaDto> kafkaTemplate;

    @Autowired
    public KafkaServiceImpl(KafkaTemplate<Long, InventoryKafkaDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void produce(InventoryKafkaDto inventoryKafkaDto) {
        kafkaTemplate.send(fallback_topic,inventoryKafkaDto);
        logger.info("Fallback message to Kafka, beneficiary - Inventory service -> '{}'", inventoryKafkaDto);

    }
}
