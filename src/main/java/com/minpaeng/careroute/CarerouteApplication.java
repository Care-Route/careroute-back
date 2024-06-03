package com.minpaeng.careroute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CarerouteApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarerouteApplication.class, args);
    }

}
