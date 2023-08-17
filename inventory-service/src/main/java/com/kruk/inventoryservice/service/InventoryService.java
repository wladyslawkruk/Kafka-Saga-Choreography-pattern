package com.kruk.inventoryservice.service;

import com.kruk.inventoryservice.domain.Item;
import com.kruk.inventoryservice.dto.ItemDto;
import com.kruk.inventoryservice.dto.PaymentKafkaDto;

import java.util.Optional;

public interface InventoryService {
    public void processPaymentMessage(PaymentKafkaDto paymentKafkaDto);
    Optional<Item> topUpStock(ItemDto itemDto);

    public void processFallbackMessage(PaymentKafkaDto paymentKafkaDto);
}
