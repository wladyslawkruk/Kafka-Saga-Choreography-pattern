package com.kruk.orderservice.consumer;

import com.kruk.orderservice.domain.OrderKafkaDto;
import com.kruk.orderservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KafkaConsumerImpl implements KafkaConsumer{

    private final OrderService orderService;

    @Autowired
    public KafkaConsumerImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerImpl.class);


    private static final String orderGroupId = "orderGroupId";

    @Override
    @Transactional
    @KafkaListener(topics = "fallback_to_order", groupId = orderGroupId,
            properties = {"spring.json.value.default.type=com.kruk.orderservice.domain.OrderKafkaDto"})
    public void consumePayment(OrderKafkaDto orderKafkaDto) {
        logger.info("Fallback message consumed: "+orderKafkaDto);
        orderService.processFallbackMessage(orderKafkaDto);

    }

}
