package com.kruk.deliveryservice.service;

import com.kruk.deliveryservice.domain.Delivery;
import com.kruk.deliveryservice.domain.OrderStatus;
import com.kruk.deliveryservice.domain.ServiceName;
import com.kruk.deliveryservice.dto.InventoryKafkaDto;
import com.kruk.deliveryservice.repository.DeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeliveryServiceImpl implements DeliveryService{


    private final DeliveryRepository deliveryRepository;

    private static final Logger logger = LoggerFactory.getLogger(DeliveryServiceImpl.class);

    private final KafkaService kafkaService;



    @Autowired
    public DeliveryServiceImpl(DeliveryRepository deliveryRepository, KafkaService kafkaService) {
        this.deliveryRepository = deliveryRepository;
        this.kafkaService = kafkaService;
    }

    @Override
    public void processInventoryMessage(InventoryKafkaDto inventoryKafkaDto) {
        Delivery delivery = Delivery.toDelivery(inventoryKafkaDto);
        delivery.setStatus(deliveryPredict().toString());
        deliveryRepository.save(delivery);
        System.out.println("Saved to db");
        InventoryKafkaDto fallbackIKO = inventoryKafkaDto;
        fallbackIKO.setStatus(OrderStatus.valueOf(delivery.getStatus()));
        fallbackIKO.setServiceName(ServiceName.DELIVERY_SERVICE);
        fallbackIKO.setModifiedTime(LocalDateTime.now().toString());
        kafkaService.produce(fallbackIKO);



    }
    private OrderStatus deliveryPredict(){
        try {
            Thread.sleep(1000);
            return Math.random()>=0.85?OrderStatus.DELIVERY_FAILED:OrderStatus.DELIVERED;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
