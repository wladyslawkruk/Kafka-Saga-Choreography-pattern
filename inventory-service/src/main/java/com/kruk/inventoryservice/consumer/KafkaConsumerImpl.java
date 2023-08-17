package com.kruk.inventoryservice.consumer;

import com.kruk.inventoryservice.dto.InventoryKafkaDto;
import com.kruk.inventoryservice.dto.PaymentKafkaDto;
import com.kruk.inventoryservice.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KafkaConsumerImpl implements KafkaConsumer{


    private final InventoryService inventoryService;

    @Autowired
    public KafkaConsumerImpl(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumerImpl.class);
    @Override
    @Transactional
    @KafkaListener(topics = "payment", groupId = "paymentGroupId",
            properties = {"spring.json.value.default.type=com.kruk.inventoryservice.dto.PaymentKafkaDto"})
    public void consumePayment(PaymentKafkaDto paymentKafkaDto) {
        logger.info("Message has been consumed: "+paymentKafkaDto);
        inventoryService.processPaymentMessage(paymentKafkaDto);
    }

    @Override
    @Transactional
    @KafkaListener(topics = "fallback_to_inventory", groupId = "inventoryGroupId",
            properties = {"spring.json.value.default.type=com.kruk.inventoryservice.dto.InventoryKafkaDto"})
    public void consumeDelivery(InventoryKafkaDto inventoryKafkaDto) {
        logger.info("Fallback message has been consumed: "+inventoryKafkaDto);
        inventoryService.processFallbackMessage(PaymentKafkaDto.toPKD(inventoryKafkaDto));
    }
}
