package com.minpaeng.careroute.domain.alarm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PersonalAlarmSendRequest {
    private int toId;
    private String title;
    private String content;
}
