package com.kruk.orderservice.service;

import com.kruk.orderservice.domain.OrderDto;
import com.kruk.orderservice.domain.OrderKafkaDto;
import com.kruk.orderservice.domain.StatusDto;
import com.kruk.orderservice.domain.Order;

import java.util.Optional;

public interface OrderService {

    Optional<Order> addOrder(OrderDto orderDto, String header);

    void updateOrderStatus(Long id, StatusDto statusDto);

    void processFallbackMessage(OrderKafkaDto orderKafkaDto);
}
