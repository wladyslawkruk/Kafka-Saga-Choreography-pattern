package com.kruk.authservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.kruk.authservice.dto.UserKafkaDto;

@Service
public class KafkaServiceImpl implements KafkaService{
    @Value("${topic}")
    private String kafkaTopic;

    private final KafkaTemplate<Long, UserKafkaDto> kafkaTemplate;

    public KafkaServiceImpl(KafkaTemplate<Long, UserKafkaDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private static final Logger logger = LoggerFactory.getLogger(KafkaServiceImpl.class);

    @Override
    public void produce(UserKafkaDto userKafkaDto) {
        kafkaTemplate.send(kafkaTopic,userKafkaDto);
        logger.info("Sent message to Kafka :'{}' , beneficiary -->PaymentS.", userKafkaDto);

    }
}
