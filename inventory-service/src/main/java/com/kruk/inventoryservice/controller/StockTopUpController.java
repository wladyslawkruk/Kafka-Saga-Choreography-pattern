package com.kruk.inventoryservice.controller;

import com.kruk.inventoryservice.dto.ItemDto;
import com.kruk.inventoryservice.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class StockTopUpController {

    private final InventoryService inventoryService;

    @Autowired
    public StockTopUpController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Operation(summary = "Top up the stock", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/topupstock")
    public ResponseEntity<?> topUp(@Valid @RequestBody ItemDto itemDto){
        return inventoryService.topUpStock(itemDto)
                .map(item -> ResponseEntity.status(HttpStatus.CREATED).body(item))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build());


    }

}
