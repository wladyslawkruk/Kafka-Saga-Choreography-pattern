package com.kruk.orderservice.domain;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class OrderDto {

    private String description;

    private String departureAddress;

    private String destinationAddress;

    private Long cost;
    private Long amount;
}
