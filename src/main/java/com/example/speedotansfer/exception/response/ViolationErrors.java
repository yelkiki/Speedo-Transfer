package com.example.speedotansfer.exception.response;


import lombok.Builder;

@Builder
public record ViolationErrors(String fieldName, String message) {
}
