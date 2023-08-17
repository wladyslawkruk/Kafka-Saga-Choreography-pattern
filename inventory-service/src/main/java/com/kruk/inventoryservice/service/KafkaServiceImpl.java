package com.kruk.inventoryservice.service;

import com.kruk.inventoryservice.dto.InventoryKafkaDto;
import com.kruk.inventoryservice.dto.PaymentKafkaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceImpl implements KafkaService{

    @Value("${topic}")
    private String inventory_topic;
    private String fallback_topic = "fallback_to_payment";
    private static final Logger logger = LoggerFactory.getLogger(KafkaServiceImpl.class);

    private final KafkaTemplate<Long, InventoryKafkaDto> kafkaTemplate;

    private final KafkaTemplate<Long, PaymentKafkaDto> fallbackKafkaTemplate;

    @Autowired
    public KafkaServiceImpl(KafkaTemplate<Long, InventoryKafkaDto> kafkaTemplate, KafkaTemplate<Long, PaymentKafkaDto> fallbackKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.fallbackKafkaTemplate = fallbackKafkaTemplate;
    }

    @Override
    public void produce(InventoryKafkaDto inventoryKafkaDto) {
        kafkaTemplate.send(inventory_topic,inventoryKafkaDto);
        logger.info("Sent message to Kafka, beneficiary - Delivery service -> '{}'",inventoryKafkaDto);
    }

    @Override
    public void fallback(PaymentKafkaDto paymentKafkaDto) {
        fallbackKafkaTemplate.send(fallback_topic,paymentKafkaDto);
        logger.info("FALLBACK message to Kafka, beneficiary - PaymentS -> '{}'", paymentKafkaDto);

    }
}
