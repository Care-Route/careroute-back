package com.minpaeng.careroute.domain.alarm.controller;

import com.minpaeng.careroute.domain.alarm.dto.request.PersonalAlarmSendRequest;
import com.minpaeng.careroute.domain.alarm.service.AlarmService;
import com.minpaeng.careroute.global.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alarm")
@RequiredArgsConstructor
@Tag(name = "Alarm", description = "알림 API")
public class AlarmController {
    private final AlarmService alarmService;

    @Operation(summary = "푸시 알림 전송", description = "푸시 알림을 전송하는 API")
    @PostMapping
    public BaseResponse pushMessagePersonal(@RequestBody PersonalAlarmSendRequest request) {
        return alarmService.sendMessageToPersonal(request);
    }
}
