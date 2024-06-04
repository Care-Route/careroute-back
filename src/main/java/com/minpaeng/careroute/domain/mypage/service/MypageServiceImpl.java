package com.minpaeng.careroute.domain.mypage.service;

import com.minpaeng.careroute.domain.member.repository.MemberRepository;
import com.minpaeng.careroute.domain.member.repository.entity.Member;
import com.minpaeng.careroute.global.dto.BaseResponse;
import com.minpaeng.careroute.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MypageServiceImpl implements MypageService {
    private final MemberRepository memberRepository;

    @Override
    public BaseResponse changeNickname(String socialId, String nickname) {
        Member member = memberRepository.findMemberBySocialId(socialId)
                .orElseThrow(() -> CustomException.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(HttpStatus.NO_CONTENT.value())
                        .message("해당되는 유저가 없습니다.")
                        .build());

        member.setNickname(nickname);
        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("닉네임이 변경되었습니다: " + nickname)
                .build();
    }

    @Override
    public BaseResponse changeAddress(String socialId, String address) {
        return null;
    }

    @Override
    public BaseResponse changeProfilePhoto(String socialId, String profilePhoto) {
        return null;
    }
}
