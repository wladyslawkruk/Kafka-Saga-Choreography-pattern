package com.kruk.orderservice.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class OrderKafkaDto {

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


    public static OrderKafkaDto toKafkaDto(Order order) {

        return new OrderKafkaDto(
                order.getId(),
                order.getStatus(),
                order.getCreationTime().toString(),
                order.getModifiedTime().toString(),
                order.getCustomerName(),
                order.getAmount(),
                order.getPrice(),
                order.getTotalCost(),
                order.getDescription(),
                ServiceName.ORDER_SERVICE
        );

    }
}
