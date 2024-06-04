package com.minpaeng.careroute.global.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@Builder
public class ExceptionResponseDto {
    private int statusCode;
    private String message;
}