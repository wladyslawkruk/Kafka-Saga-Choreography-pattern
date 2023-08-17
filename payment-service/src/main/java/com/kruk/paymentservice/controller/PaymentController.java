package com.kruk.paymentservice.controller;

import com.kruk.paymentservice.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @Operation(summary = "Top up the balance", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/topup")
    public ResponseEntity<?> topUp(@RequestParam("amount") Long input, @RequestHeader("Authorization") String header ){
        System.out.println(paymentService.retrieveUserNameFromToken(header));
        paymentService.topUpBalance(input,paymentService.retrieveUserNameFromToken(header));
        return ResponseEntity.status(HttpStatus.CREATED).body("Balance of user "+
                paymentService.retrieveUserNameFromToken(header)
                + " has been topped up with "+input+ " coins");


    }

}


