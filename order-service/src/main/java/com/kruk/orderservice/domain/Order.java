package com.kruk.orderservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "departure_address")
    private String departureAddress;

    @Column(name = "destination_address")
    private String destinationAddress;

    @Column(name = "price")
    private Long price;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "total_cost")
    private Long totalCost;

    @CreationTimestamp
    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @UpdateTimestamp
    @Column(name = "modified_time")
    private LocalDateTime modifiedTime;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderStatusHistory> orderStatusHistory = new ArrayList<>();

    public Order(
            String departureAddress,
            String destinationAddress,
            String description,
            Long price,
            OrderStatus status,
            Long amount,
            Long totalCost

    ) {
        this.departureAddress = departureAddress;
        this.destinationAddress = destinationAddress;
        this.description = description;
        this.price = price;
        this.status = status;
        this.creationTime = LocalDateTime.now();
        this.modifiedTime = creationTime;
        this.amount = amount;
        this.totalCost = totalCost;
    }

    public void addStatusHistory(OrderStatus status, ServiceName serviceName, String comment) {
        getOrderStatusHistory().add(new OrderStatusHistory(null, status, serviceName, comment, this));
    }
}
