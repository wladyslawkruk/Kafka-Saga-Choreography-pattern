package com.kruk.inventoryservice.service;

import com.kruk.inventoryservice.domain.Item;
import com.kruk.inventoryservice.domain.OrderStatus;
import com.kruk.inventoryservice.domain.ServiceName;
import com.kruk.inventoryservice.dto.InventoryKafkaDto;
import com.kruk.inventoryservice.dto.ItemDto;
import com.kruk.inventoryservice.dto.PaymentKafkaDto;
import com.kruk.inventoryservice.exception.ItemNotFoundException;
import com.kruk.inventoryservice.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService{

    private final InventoryRepository inventoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private final KafkaService kafkaService;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository, KafkaService kafkaService) {
        this.inventoryRepository = inventoryRepository;
        this.kafkaService = kafkaService;
    }

    @Override
    public void processPaymentMessage(PaymentKafkaDto paymentKafkaDto) {

        System.out.println("Payment message is being processed...");
        Item it = inventoryRepository.findByName(paymentKafkaDto.getDescription())
                .orElseThrow(()->new ItemNotFoundException(paymentKafkaDto.getDescription()));

        if(it==null){
            System.out.println("item not found=(");
            PaymentKafkaDto fallback = paymentKafkaDto;
            fallback.setModifiedTime(LocalDateTime.now().toString());
            fallback.setStatus(OrderStatus.UNEXPECTED_FAILURE);
            fallback.setServiceName(ServiceName.INVENTORY_SERVICE);
            kafkaService.fallback(fallback);
        }
        else{
            if(it.getStock()>= paymentKafkaDto.getAmount()){
                System.out.println("seems like enough stock");
                it.setStock(it.getStock()- paymentKafkaDto.getAmount());
                InventoryKafkaDto inventoryKafkaDto = InventoryKafkaDto.toIKD(paymentKafkaDto);
                inventoryKafkaDto.setModifiedTime(LocalDateTime.now().toString());
                inventoryKafkaDto.setStatus(OrderStatus.INVENTED);
                inventoryKafkaDto.setServiceName(ServiceName.INVENTORY_SERVICE);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                kafkaService.produce(inventoryKafkaDto);
                inventoryRepository.save(it);

            }
            else{
                PaymentKafkaDto fallback = paymentKafkaDto;
                fallback.setModifiedTime(LocalDateTime.now().toString());
                fallback.setStatus(OrderStatus.INVENTMENT_FAILED);
                fallback.setServiceName(ServiceName.INVENTORY_SERVICE);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                kafkaService.fallback(fallback);

            }
        }

    }

    @Override
    public Optional<Item> topUpStock(ItemDto itemDto) {
        Item item = inventoryRepository.findByName(itemDto.getItemName()).orElse(new Item
                (itemDto.getItemName(),(long)itemDto.getPrice(),0l));
        item.setStock(item.getStock()+itemDto.getAmount());
        item.setPrice((long)itemDto.getPrice());

        inventoryRepository.save(item);
        return Optional.of(item);
    }

    @Override
    public void processFallbackMessage(PaymentKafkaDto paymentKafkaDto) {
        if(paymentKafkaDto.getStatus().equals(OrderStatus.DELIVERY_FAILED)){
            Item it = inventoryRepository.findByName(paymentKafkaDto.getDescription()).get();
            it.setStock(it.getStock()+ paymentKafkaDto.getAmount());
            inventoryRepository.save(it);
        }

        kafkaService.fallback(paymentKafkaDto);
    }


}
