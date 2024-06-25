package com.minpaeng.careroute.domain.route.controller;

import com.minpaeng.careroute.domain.route.request.TestWebSocketRequest;
import com.minpaeng.careroute.domain.routine.dto.request.PositionShareRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RouteWebSocketController {
//    private final SimpMessagingTemplate template;
//
////    @MessageMapping(value = "/{memberId}")
////    public void testWebSocket(@DestinationVariable int memberId,
////                            @Payload TestWebSocketRequest request) {
////        log.info("웹 소켓 수신: " + request.getContent() + " " + memberId);
////
////        Map<String, Object> headers = Map.of("success", true, "type", "test");
////        template.convertAndSend("/sub/" + memberId, request, headers);
////        log.info("웹 소켓 발신 완료: " + memberId);
////    }
////
//////    @SubscribeMapping("/sub/{memberId}")
//////    public void handleSubscription(@DestinationVariable String memberId) {
//////        log.info("구독 수신: memberId" + memberId);
//////    }
//
//    @MessageMapping(value = "/{memberId}/route")
//    public void positionShare(@DestinationVariable int memberId,
//                              @Payload PositionShareRequest request) {
//        log.info("위치 공유 웹 소켓 수신: " + request.getLatitude() + " " + request.getLongitude()
//                + " " + memberId);
//
//        Map<String, Object> headers = Map.of("success", true, "type", "positionShare");
//        template.convertAndSend("/sub/" + memberId, request, headers);
//        log.info("위치 공유 소켓 발신 완료: " + memberId);
//    }
}
