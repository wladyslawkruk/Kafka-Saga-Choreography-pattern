package com.kruk.inventoryservice.dto;

import com.kruk.inventoryservice.domain.OrderStatus;
import com.kruk.inventoryservice.domain.ServiceName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryKafkaDto {
    private Long id;

    private OrderStatus status;

    private String creationTime;

    private String modifiedTime;
    private String orderedBy;
    private Long amount;
    private Long price;
    private Long totalCost;
    private String description;
    private ServiceName serviceName;

    public static InventoryKafkaDto toIKD(PaymentKafkaDto paymentKafkaDto){
        return new InventoryKafkaDto(
                paymentKafkaDto.getId(),
                paymentKafkaDto.getStatus(),
                paymentKafkaDto.getCreationTime(),
                paymentKafkaDto.getModifiedTime(),
                paymentKafkaDto.getOrderedBy(),
                paymentKafkaDto.getAmount(),
                paymentKafkaDto.getPrice(),
                paymentKafkaDto.getTotalCost(),
                paymentKafkaDto.getDescription(),
                paymentKafkaDto.getServiceName()
        );
    }
}
