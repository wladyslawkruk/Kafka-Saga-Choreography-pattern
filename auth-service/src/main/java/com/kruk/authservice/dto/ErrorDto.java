package com.kruk.authservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorDto {

    private String errorMessage;

    private LocalDateTime timestamp;

    public ErrorDto(String errorMessage, LocalDateTime timestamp) {
        this.errorMessage = errorMessage;
        this.timestamp = timestamp;
    }
}
