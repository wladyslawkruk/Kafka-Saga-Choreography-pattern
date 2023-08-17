package com.kruk.orderservice.controller;

import com.kruk.orderservice.domain.OrderDto;
import com.kruk.orderservice.domain.StatusDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kruk.orderservice.domain.Order;
import com.kruk.orderservice.repository.OrderRepository;
import com.kruk.orderservice.service.OrderService;

import java.util.List;

@Slf4j
@RestController
public class OrderController {

    private final OrderRepository orderRepository;

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @Operation(summary = "List all orders in delivery system", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/order")
    public List<Order> listOrders() {
        return orderRepository.findAll();
    }

    @Operation(summary = "Get an order in system by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/order/{orderId}")
    public Order listOrder(@PathVariable @Parameter(description = "Id of order") Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Operation(summary = "Add order and start delivery process for it", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/order")
    public ResponseEntity<?> addOrder(@RequestBody OrderDto input, @RequestHeader("Authorization") String header) {
        return orderService.addOrder(input,header)
                .map(order -> ResponseEntity.status(HttpStatus.CREATED).body(order))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build());
    }

    @Operation(summary = "Update order status", security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping("/order/{orderId}")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable @Parameter(description = "Id of order") long orderId,
                                               @RequestBody StatusDto statusDto) {
        try {
            orderService.updateOrderStatus(orderId, statusDto);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            log.error("Can't change status for order with id {}", orderId, ex);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }
}
