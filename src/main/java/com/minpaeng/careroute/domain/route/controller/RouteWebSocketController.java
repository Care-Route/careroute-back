package com.minpaeng.careroute.domain.route.controller;

import com.minpaeng.careroute.domain.route.request.TestWebSocketRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RouteWebSocketController {
    private final SimpMessagingTemplate template;

    @MessageMapping(value = "/{memberId}")
    public void testWebSocket(@DestinationVariable int memberId,
                            @Payload TestWebSocketRequest request) {
        log.info("웹 소켓 수신: " + request.getContent() + " " + memberId);

        Map<String, Object> headers = Map.of("success", true, "type", "buyBuilding");
        template.convertAndSend("/sub/" + memberId, request, headers);
        log.info("웹 소켓 발신 완료: " + memberId);
    }

    @SubscribeMapping("/sub/{memberId}")
    public void handleSubscription(@DestinationVariable String memberId) {
        log.info("구독 수신: memberId" + memberId);
    }
}
