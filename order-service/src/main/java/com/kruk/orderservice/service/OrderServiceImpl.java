package com.kruk.orderservice.service;

import com.kruk.orderservice.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kruk.orderservice.controller.OrderNotFoundException;
import com.kruk.orderservice.repository.OrderRepository;

import java.util.Base64;
import java.util.Optional;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final KafkaService kafkaService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, KafkaService kafkaService) {
        this.orderRepository = orderRepository;
        this.kafkaService = kafkaService;
    }

    @Transactional
    @Override
    public Optional<Order> addOrder(OrderDto orderDto, String header) {
        Order newOrder = new Order(
                orderDto.getDepartureAddress(),
                orderDto.getDestinationAddress(),
                orderDto.getDescription(),
                orderDto.getCost(),
                OrderStatus.REGISTERED,
                orderDto.getAmount(),
                orderDto.getCost()* orderDto.getAmount()

        );
        newOrder.setCustomerName(retrieveUserNameFromToken(header));
        newOrder.addStatusHistory(newOrder.getStatus(), ServiceName.ORDER_SERVICE, "Order created");
        Order order = orderRepository.save(newOrder);
        kafkaService.produce(OrderKafkaDto.toKafkaDto(order));
        return Optional.of(order);
    }

    @Transactional
    @Override
    public void updateOrderStatus(Long id, StatusDto statusDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        if (order.getStatus() == statusDto.getStatus()) {
            log.info("Request with same status {} for order {} from service {}", statusDto.getStatus(), id, statusDto.getServiceName());
            return;
        }
        order.setStatus(statusDto.getStatus());
        order.addStatusHistory(statusDto.getStatus(), statusDto.getServiceName(), statusDto.getComment());
        orderRepository.save(order);
       // Order resultOrder = orderRepository.save(order);
       // kafkaService.produce(OrderKafkaDto.toKafkaDto(resultOrder));
    }

    @Override
    public void processFallbackMessage(OrderKafkaDto orderKafkaDto) {
        updateOrderStatus(orderKafkaDto.getId(),new StatusDto(
                orderKafkaDto.getStatus(),orderKafkaDto.getServiceName()
                ,"Status changed by "+orderKafkaDto.getServiceName().name())
        );

    }

    public String retrieveUserNameFromToken(String token){
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String temp= new String(decoder.decode(chunks[1]));
        return org.apache.commons.lang3.StringUtils.chop(temp.substring(8,temp.indexOf(',')));
    }
}
