package com.kruk.inventoryservice.consumer;

import com.kruk.inventoryservice.dto.InventoryKafkaDto;
import com.kruk.inventoryservice.dto.PaymentKafkaDto;

public interface KafkaConsumer {
    public void consumePayment(PaymentKafkaDto paymentKafkaDto);
    public void consumeDelivery(InventoryKafkaDto inventoryKafkaDto);


}
