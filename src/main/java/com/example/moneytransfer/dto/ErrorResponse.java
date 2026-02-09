package com.example.moneytransfer.dto;

import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

/**
 * Standard error response returned by the API.
 */
@Value
@Builder
public class ErrorResponse {

    OffsetDateTime timestamp;

    int status;

    String error;

    String message;

    String path;
}

