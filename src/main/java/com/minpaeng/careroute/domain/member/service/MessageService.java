package com.minpaeng.careroute.domain.member.service;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
public class MessageService {
    @Value("${coolsms.apikey}")
    private String apiKey;

    @Value("${coolsms.apisecret}")
    private String apiSecret;

    @Value("${coolsms.fromnumber}")
    private String fromNumber;

    private HashMap<String, String> makeParams(String to, String message) {
        HashMap<String, String> params = new HashMap<>();
        params.put("from", fromNumber);
        params.put("type", "SMS");
        params.put("app_version", "test app 1.2");
        params.put("to", to);
        params.put("text", message);
        return params;
    }

    public void sendSMSForAuth(String phonNumber, String randomNum) {
        Message coolsms = new Message(apiKey, apiSecret);
        String message = "Careroute 전화번호 인증 코드: " + randomNum;
        sendMessage(phonNumber, coolsms, message);
    }

    public void sendSMSForConnection(String phonNumber) {
        Message coolsms = new Message(apiKey, apiSecret);
        String message = "Careroute 기기연결 요청 알림 메세지입니다. 앱 설치 및 가입 후 기기 연결을 진행해주세요.";
        sendMessage(phonNumber, coolsms, message);
    }

    private void sendMessage(String phonNumber, Message coolsms, String message) {
        HashMap<String, String> params = makeParams(phonNumber, message);
        try {
            JSONObject obj = coolsms.send(params);
            log.info(obj.toString());
        } catch (CoolsmsException e) {
            log.info("메세지 발신 에러: " +e.getMessage());
            log.info("에러 코드: " + e.getCode());
        }
    }
}