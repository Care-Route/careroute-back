package com.minpaeng.careroute.domain.mypage.service;

import com.minpaeng.careroute.domain.member.repository.MemberRepository;
import com.minpaeng.careroute.domain.member.repository.entity.Member;
import com.minpaeng.careroute.global.dto.BaseResponse;
import com.minpaeng.careroute.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MypageServiceImpl implements MypageService {
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public BaseResponse changeNickname(String socialId, String nickname) {
        Member member = getMemberBySocialId(socialId);
        member.setNickname(nickname);

        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("닉네임이 변경되었습니다: " + nickname)
                .build();
    }

    @Transactional
    @Override
    public BaseResponse changeAddress(String socialId, String address) {
        Member member = getMemberBySocialId(socialId);
        member.setAddress(address);

        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("주소가 변경되었습니다: " + address)
                .build();
    }

    @Transactional
    @Override
    public BaseResponse changeProfilePhoto(String socialId, String profilePhoto) {
        return null;
    }

    private Member getMemberBySocialId(String socialId) {
        return memberRepository.findMemberBySocialId(socialId)
                .orElseThrow(() -> CustomException.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(HttpStatus.NO_CONTENT.value())
                        .message("해당되는 유저가 없습니다.")
                        .build());
    }
}
