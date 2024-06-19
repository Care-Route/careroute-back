package com.minpaeng.careroute.global.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SMSAuthEvent {
    private String phoneNumber;
    private String randomNum;
}
