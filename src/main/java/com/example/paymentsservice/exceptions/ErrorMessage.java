package com.example.paymentsservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorMessage {

    private String message;
    private ZonedDateTime timestamp;

    public ErrorMessage(String message) {
        this.message = message;
        this.timestamp = ZonedDateTime.now();
    }
}