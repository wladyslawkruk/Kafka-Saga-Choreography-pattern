package com.kruk.deliveryservice.domain;

import com.kruk.deliveryservice.dto.InventoryKafkaDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor

@Entity
@Data
@Table(name = "Deliveries")
public class Delivery {

    public Delivery(String userName, Long cost, String status, Long amount, String itemName) {
        this.userName = userName;
        this.cost = cost;
        this.status = status;
        this.amount = amount;
        this.itemName = itemName;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "beneficiary",nullable = false)
    private String userName;

    @Column(name = "cost")
    private Long cost;

    @Column(name = "delivery_status")
    private String status;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "item_name")
    private String itemName;



    public static Delivery toDelivery(InventoryKafkaDto inventoryKafkaDto){
        return new Delivery(
                inventoryKafkaDto.getOrderedBy(),
                inventoryKafkaDto.getTotalCost(),
                inventoryKafkaDto.getStatus().toString(),
                inventoryKafkaDto.getAmount(),
                inventoryKafkaDto.getDescription()
        );
    }


}
