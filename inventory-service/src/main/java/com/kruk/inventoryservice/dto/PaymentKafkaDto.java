package com.kruk.inventoryservice.dto;


import com.kruk.inventoryservice.domain.OrderStatus;
import com.kruk.inventoryservice.domain.ServiceName;
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

    public static PaymentKafkaDto toPKD(InventoryKafkaDto inventoryKafkaDto){

      return new PaymentKafkaDto(
              inventoryKafkaDto.getId(),
              inventoryKafkaDto.getStatus(),
              inventoryKafkaDto.getCreationTime(),
              inventoryKafkaDto.getModifiedTime(),
              inventoryKafkaDto.getOrderedBy(),
              inventoryKafkaDto.getAmount(),
              inventoryKafkaDto.getPrice(),
              inventoryKafkaDto.getTotalCost(),
              inventoryKafkaDto.getDescription(),
              inventoryKafkaDto.getServiceName()
      );

    }

}
