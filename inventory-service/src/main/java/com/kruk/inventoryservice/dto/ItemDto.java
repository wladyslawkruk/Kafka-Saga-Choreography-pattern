package com.kruk.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDto {

    private String itemName;

    private Long price;

    private Long amount;



}

