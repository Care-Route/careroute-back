package com.minpaeng.careroute.domain.member.service;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Slf4j
@Service
public class MessageService {
    @Value("${coolsms.apikey}")
    private String apiKey;

    @Value("${coolsms.apisecret}")
    private String apiSecret;

    @Value("${coolsms.fromnumber}")
    private String fromNumber;

    private String createRandomNumber() {
        Random rand = new Random();
        StringBuilder randomNum = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            String random = Integer.toString(rand.nextInt(10));
            randomNum.append(random);
        }

        return randomNum.toString();
    }

    private HashMap<String, String> makeParams(String to, String randomNum) {
        HashMap<String, String> params = new HashMap<>();
        params.put("from", fromNumber);
        params.put("type", "SMS");
        params.put("app_version", "test app 1.2");
        params.put("to", to);
        params.put("text", randomNum);
        return params;
    }

    public void sendSMS(String phonNumber) {
        Message coolsms = new Message(apiKey, apiSecret);
        String randomNum = createRandomNumber();
        HashMap<String, String> params = makeParams(phonNumber, randomNum);

        try {
            JSONObject obj = coolsms.send(params);
            log.info(obj.toString());
        } catch (CoolsmsException e) {
            log.info("메세지 발신 에러: " +e.getMessage());
            log.info("에러 코드: " + e.getCode());
        }
    }
}