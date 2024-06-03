package com.minpaeng.careroute.domain.member.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minpaeng.careroute.domain.member.repository.redis.OIDCPublicKeysRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class KakaoOauthHelper {
    @Value("${oauth.base-url.kakao}")
    private String kakaoBaseUrl;

    @Value("${oauth.app-id.kakao}")
    private String kakaoAppId;

    private final OauthOIDCHelper oauthOIDCHelper;
    private final OIDCPublicKeysRepository oidcPublicKeysRepository;

    @Transactional
    public OIDCDecodePayload getOIDCDecodePayload(String token) {
        System.out.println("페이로드디코드 진입");
        OIDCPublicKeysResponse oidcPublicKeysResponse = getKakaoOIDCOpenKeys();
        return oauthOIDCHelper.getPayloadFromIdToken(
                token,
                kakaoBaseUrl,
                kakaoAppId,
                oidcPublicKeysResponse);
    }

    @Transactional
    public OIDCPublicKeysResponse getKakaoOIDCOpenKeys() {
        RestClient restClient = RestClient.create();
        Optional<OIDCPublicKeysResponse> responseOptional =
                oidcPublicKeysRepository.findOIDCPublicKeysResponseByName("KakaoOIDC");
        OIDCPublicKeysResponse result;
        if (responseOptional.isEmpty()) {
            System.out.println("요청 보내는중");
            result = restClient.get()
                    .uri("https://kauth.kakao.com/.well-known/jwks.json")
                    .exchange((request, response) -> {
                        System.out.println(response.getBody());
                        if (response.getStatusCode().isError()) {
                            System.out.println("요청 실패");
                            throw new IllegalStateException("공개키 조회 에러");
                        }
                        else {
                            System.out.println("요청 성공: " + response.getBody().toString());
//                       ResponseEntity<List<OIDCPublicKeyDto>> a = response.getBody().
                            ObjectMapper objectMapper = new ObjectMapper();
                            return objectMapper.readValue(response.getBody(), new TypeReference<>() {});
//                       return null;
                        }
                    });
            oidcPublicKeysRepository.save(result);
        } else result = responseOptional.get();
        System.out.println(result);
        return result;
    }
}