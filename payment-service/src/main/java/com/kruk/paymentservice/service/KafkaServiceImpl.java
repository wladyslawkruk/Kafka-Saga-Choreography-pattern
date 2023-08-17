package com.kruk.paymentservice.service;

import com.kruk.paymentservice.dto.OrderKafkaDto;
import com.kruk.paymentservice.dto.PaymentKafkaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceImpl implements KafkaService{

    @Value("${topic}")
    private String payment_topic;

    private String fallback_topic = "fallback_to_order";

    private static final Logger logger = LoggerFactory.getLogger(KafkaServiceImpl.class);

    private final KafkaTemplate<Long, PaymentKafkaDto> kafkaTemplate;
    private final KafkaTemplate<Long, OrderKafkaDto> fallbackKafkaTemplate;

    @Autowired
    public KafkaServiceImpl(KafkaTemplate<Long, PaymentKafkaDto> kafkaTemplate,
                            KafkaTemplate<Long, OrderKafkaDto> fallbackKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.fallbackKafkaTemplate = fallbackKafkaTemplate;
    }

    @Override
    public void produce(PaymentKafkaDto paymentKafkaDto) {
        kafkaTemplate.send(payment_topic,paymentKafkaDto);
        logger.info("Sent message to Kafka, beneficiary - Inventory service -> '{}'", paymentKafkaDto);
    }

    @Override
    public void fallback(OrderKafkaDto orderKafkaDto) {
        fallbackKafkaTemplate.send(fallback_topic,orderKafkaDto);
        logger.info("FALLBACK message to Kafka, beneficiary - OrderS -> '{}'", orderKafkaDto);
    }
}
