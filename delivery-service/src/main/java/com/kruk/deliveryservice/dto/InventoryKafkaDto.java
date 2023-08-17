package com.kruk.deliveryservice.dto;

import com.kruk.deliveryservice.domain.OrderStatus;

import com.kruk.deliveryservice.domain.ServiceName;
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


}
