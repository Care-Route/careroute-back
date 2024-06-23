package com.minpaeng.careroute.domain.alarm.service;

import com.minpaeng.careroute.domain.alarm.dto.request.PersonalAlarmSendRequest;
import com.minpaeng.careroute.global.dto.BaseResponse;

public interface AlarmService {
    BaseResponse sendMessageToPersonal(PersonalAlarmSendRequest request);
}
