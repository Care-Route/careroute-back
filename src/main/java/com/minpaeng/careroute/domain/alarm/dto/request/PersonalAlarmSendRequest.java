package com.minpaeng.careroute.domain.alarm.dto.request;

import lombok.Getter;

@Getter
public class PersonalAlarmSendRequest {
    private int toId;
    private String title;
    private String content;
}
