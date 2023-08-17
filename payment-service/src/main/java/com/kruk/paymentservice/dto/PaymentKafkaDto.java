package com.kruk.paymentservice.dto;

import com.kruk.paymentservice.domain.OrderStatus;
import com.kruk.paymentservice.domain.ServiceName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentKafkaDto {
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

    public static PaymentKafkaDto toPKD(OrderKafkaDto okd){
        return new PaymentKafkaDto(
                okd.getId(),
                okd.getStatus(),
                okd.getCreationTime(),
                okd.getModifiedTime(),
                okd.getOrderedBy(),
                okd.getAmount(),
                okd.getPrice(),
                okd.getTotalCost(),
                okd.getDescription(),
                ServiceName.PAYMENT_SERVICE
        );

    }
}
