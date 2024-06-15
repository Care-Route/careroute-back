package com.minpaeng.careroute.domain.mypage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.minpaeng.careroute.domain.member.repository.MemberRepository;
import com.minpaeng.careroute.domain.member.repository.entity.Member;
import com.minpaeng.careroute.domain.mypage.dto.response.ProfileImageUpdateResponse;
import com.minpaeng.careroute.domain.mypage.dto.response.ProfileInfoResponse;
import com.minpaeng.careroute.global.dto.BaseResponse;
import com.minpaeng.careroute.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MypageServiceImpl implements MypageService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private final MemberRepository memberRepository;

    @Override
    public ProfileInfoResponse getProfileInfo(String socialId) {
        Member member = getMemberBySocialId(socialId);
        return new ProfileInfoResponse(member);
    }

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
    public ProfileImageUpdateResponse changeProfilePhoto(String socialId, MultipartFile image) {
        Member member = getMemberBySocialId(socialId);
        try {
            String url = uploadImage(image);
            member.setProfileImagePath(url);
            return ProfileImageUpdateResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("프로필 이미지 변경 완료")
                    .imageUrl(url)
                    .build();
        } catch (IOException e) {
            throw CustomException.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("프로필 이미지 저장소 업로드 실패")
                    .build();
        }
    }

    @Override
    @Transactional
    public BaseResponse changePhoneNumber(String socialId, String phoneNumber) {
        Member member = getMemberBySocialId(socialId);
        member.setPhoneNumber(phoneNumber);
        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("휴대폰 번호가 변경되었습니다: " + phoneNumber)
                .build();
    }

    private Member getMemberBySocialId(String socialId) {
        return memberRepository.findMemberBySocialId(socialId)
                .orElseThrow(() -> CustomException.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(HttpStatus.NO_CONTENT.value())
                        .message("해당되는 유저가 없습니다.")
                        .build());
    }

    private String uploadImage(MultipartFile profileImage) throws IOException {
        String s3FileName = UUID.randomUUID() + "-" + profileImage.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(profileImage.getInputStream().available());
        objMeta.setContentType(profileImage.getContentType());

        amazonS3.putObject(bucket, s3FileName, profileImage.getInputStream(), objMeta);
        log.info(String.format("사진 업로드 [파일명 : %s]", s3FileName));

        return amazonS3.getUrl(bucket, s3FileName).toString();
    }
}
