package com.minpaeng.careroute.domain.alarm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.minpaeng.careroute.domain.alarm.dto.request.PersonalAlarmSendRequest;
import com.minpaeng.careroute.domain.member.repository.MemberRepository;
import com.minpaeng.careroute.domain.member.repository.entity.Member;
import com.minpaeng.careroute.global.dto.BaseResponse;
import com.minpaeng.careroute.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {
    private final FirebaseMessaging firebaseMessaging;
    private final MemberRepository memberRepository;

    @Override
    public BaseResponse sendMessageToPersonal(PersonalAlarmSendRequest request) {
        Member to = getMember(request.getToId());

        Message message = makePersonalMessage(request, to.getFcmToken());
        String messageId = sendMessage(message);
        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("알림 전송 완료(메세지 아이디: " + messageId + ")")
                .build();
    }

    private Message makePersonalMessage(PersonalAlarmSendRequest request, String token) {
        try {
            return Message.builder()
                    .putData("title", request.getTitle())
                    .putData("content", request.getContent())
                    .setToken(token)
                    .build();
        } catch (Exception e) {
            log.error("파이어베이스 메세지 생성 오류: " + e.getMessage());
        }
        return null;
    }

    private String sendMessage(Message message) {
        try {
            String response = firebaseMessaging.send(message);
            log.info("Successfully sent message: {}", response);
            return response;
        } catch (FirebaseMessagingException e) {
            log.error("알림 전송 실패(파이어베이스 오류): " + e);
//            throw new RuntimeException("알림 전송 실패: " + e);
        }
        catch (Exception e) {
            log.error("알림 전송 실패(서버 오류): " + e.getMessage());
        }
        return "";
    }

    private Member getMember(int toId) {
        return memberRepository.findById(toId)
                .orElseThrow(this::getNotExistMemberException);
    }

    private CustomException getNotExistMemberException() {
        return CustomException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.NO_CONTENT.value())
                .message("해당하는 사용자가 존재하지 않습니다.")
                .build();
    }
}
