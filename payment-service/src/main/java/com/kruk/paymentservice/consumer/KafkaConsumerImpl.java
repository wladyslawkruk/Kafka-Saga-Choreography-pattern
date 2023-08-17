package com.kruk.paymentservice.consumer;
import com.kruk.paymentservice.dto.OrderKafkaDto;
import com.kruk.paymentservice.dto.PaymentKafkaDto;
import com.kruk.paymentservice.dto.UserKafkaDto;
import com.kruk.paymentservice.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Service
public class KafkaConsumerImpl implements KafkaConsumer{

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerImpl.class);
     private static final String orderGroupId = "orderGroupId";
    private static final String userGroupId = "userGroupId";
    private static final String paymentGroupId = "paymentGroupId";


    private final PaymentService paymentService;

    @Autowired
    public KafkaConsumerImpl(PaymentService paymentService) {
        this.paymentService = paymentService;
    }



    @Override
    @Transactional
    @KafkaListener(topics = "user", groupId = userGroupId,
            properties = {"spring.json.value.default.type=com.kruk.paymentservice.dto.UserKafkaDto"})
    public void consumeUser(UserKafkaDto userKafkaDto) {

        paymentService.saveToUserBalance(userKafkaDto);
        logger.info("Message consumed: "+userKafkaDto);
    }

    @Override
    @Transactional
    @KafkaListener(topics = "order", groupId =orderGroupId,
            properties = {"spring.json.value.default.type=com.kruk.paymentservice.dto.OrderKafkaDto"})
    public void consumeOrder(OrderKafkaDto orderKafkaDto) {
        logger.info("Message consumed: "+orderKafkaDto);
        paymentService.processOrderMessage(orderKafkaDto);


    }

    @Override
    @Transactional
    @KafkaListener(topics = "fallback_to_payment", groupId =paymentGroupId,
            properties = {"spring.json.value.default.type=com.kruk.paymentservice.dto.PaymentKafkaDto"})
    public void consumeInventory(PaymentKafkaDto paymentKafkaDto) {
        logger.info("Fallback message consumed: "+paymentKafkaDto);
        paymentService.processFallbackMessage(OrderKafkaDto.toORD(paymentKafkaDto));

    }


}
