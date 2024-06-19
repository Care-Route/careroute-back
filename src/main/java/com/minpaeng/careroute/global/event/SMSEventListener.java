package com.minpaeng.careroute.global.event;

import com.minpaeng.careroute.domain.member.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SMSEventListener {
    private final MessageService messageService;

    @EventListener
    public void sendMessage(SMSAuthEvent event) {
        messageService.sendSMSForAuth(event.getPhoneNumber(), event.getRandomNum());
        log.info(event.getPhoneNumber() + " 로 인증 번호 발송 완료: " + event.getRandomNum());
    }
}
