package com.example.speedotansfer.exception.response;


import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class ValidationFailedResponse {
    private final String message;
    private final String details;
    private final LocalDateTime timeStamp;
    private final HttpStatus httpStatus;
}
