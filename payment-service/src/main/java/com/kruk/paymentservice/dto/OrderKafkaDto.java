package com.kruk.paymentservice.dto;


import com.kruk.paymentservice.domain.OrderStatus;
import com.kruk.paymentservice.domain.ServiceName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
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


    public static OrderKafkaDto toORD(PaymentKafkaDto pkd){
        return new OrderKafkaDto(
                pkd.getId(),
                pkd.getStatus(),
                pkd.getCreationTime(),
                pkd.getModifiedTime(),
                pkd.getOrderedBy(),
                pkd.getAmount(),
                pkd.getPrice(),
                pkd.getTotalCost(),
                pkd.getDescription(),
                pkd.getServiceName()
        );

    }


}
