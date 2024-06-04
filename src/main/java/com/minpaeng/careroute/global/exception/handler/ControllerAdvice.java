package com.minpaeng.careroute.global.exception.handler;

import com.minpaeng.careroute.global.exception.CustomException;
import com.minpaeng.careroute.global.exception.ExceptionResponseDto;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(CustomException.class)
    private ResponseEntity<ExceptionResponseDto> exception(CustomException e) {

        return ResponseEntity
                .status(e.getStatus())
                .body(ExceptionResponseDto.builder()
                        .statusCode(e.getCode())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    private ResponseEntity<ExceptionResponseDto> paramException() {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponseDto.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message("필요한 요청 파라미터를 모두 입력해주세요.")
                        .build());
    }

    @ExceptionHandler(JwtException.class)
    private ResponseEntity<ExceptionResponseDto> jwtSignatureException() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponseDto.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message("유효하지 않은 JWT 토큰입니다.")
                        .build());
    }
}
