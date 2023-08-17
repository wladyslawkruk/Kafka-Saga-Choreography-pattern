package com.kruk.authservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.kruk.authservice.dto.ErrorDto;

import java.sql.SQLException;
import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    private final String UNIQUE_VIOLATION = "23505";

    @ExceptionHandler
    public ResponseEntity<ErrorDto> exceptionHandler(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorDto(ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> sqlExceptionHandler(SQLException ex) {
        if (ex.getSQLState().equals(UNIQUE_VIOLATION)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDto("User with this name already exists", LocalDateTime.now()));
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("Internal service error", LocalDateTime.now()));
    }
}
