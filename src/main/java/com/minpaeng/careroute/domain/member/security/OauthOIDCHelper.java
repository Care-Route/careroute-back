package com.minpaeng.careroute.domain.member.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minpaeng.careroute.domain.member.repository.redis.OIDCPublicKeysRepository;
import com.minpaeng.careroute.global.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OauthOIDCHelper {
    @Value("${oauth.base-url.kakao}")
    private String iss;
    @Value("${oauth.app-id.kakao}")
    private String aud;
    private final JwtOIDCProvider jwtOIDCProvider;
    private final UserDetailsService userDetailsService;
    private final OIDCPublicKeysRepository oidcPublicKeysRepository;

    @Transactional
    public OIDCDecodePayload getOIDCDecodePayload(String token) {
        log.info("페이로드디코드 진입");
        Optional<String> optionalIss = getIss(token);
        if (optionalIss.isEmpty()) throw new CustomException(HttpStatus.BAD_REQUEST, 400, "토큰에 iss 부재");
        OIDCPublicKeysResponse oidcPublicKeysResponse;
        if (optionalIss.get().contains("kakao")) oidcPublicKeysResponse = getKakaoOIDCOpenKeys();
        else oidcPublicKeysResponse = getGoogleOIDCOpenKeys();
        return getPayloadFromIdToken(
                token,
                oidcPublicKeysResponse);
    }

    @Transactional
    public OIDCPublicKeysResponse getKakaoOIDCOpenKeys() {
        RestClient restClient = RestClient.create();
        Optional<OIDCPublicKeysResponse> responseOptional =
                oidcPublicKeysRepository.findOIDCPublicKeysResponseByName("KakaoOIDC");
        OIDCPublicKeysResponse result;
        if (responseOptional.isEmpty()) {
            log.info("요청 보내는중");
            result = restClient.get()
                    .uri("https://kauth.kakao.com/.well-known/jwks.json")
                    .exchange((request, response) -> {
                        if (response.getStatusCode().isError()) {
                            log.info("카카오 공개키 요청 실패");
                            throw new IllegalStateException("공개키 조회 에러");
                        }
                        else {
                            log.info("카카오 공개키 요청 성공");
                            ObjectMapper objectMapper = new ObjectMapper();
                            return objectMapper.readValue(response.getBody(), new TypeReference<>() {});
                        }
                    });
            oidcPublicKeysRepository.save(result);
        } else result = responseOptional.get();

        log.info(result.toString());
        return result;
    }

    private OIDCPublicKeysResponse getGoogleOIDCOpenKeys() {
        return null;
    }

    // OauthOIDC는 스펙이기때문에 OauthOIDCHelper 하나로 카카오,구글 다 대응 가능하다.
    // KakaoOauthHelper 등에서 아래 소스들을 사용한다.
    // kid를 토큰에서 가져온다.

    private String getKidFromUnsignedIdToken(String token, String iss, String aud) {
        return jwtOIDCProvider.getKidFromUnsignedTokenHeader(token, iss, aud);
    }

    public OIDCDecodePayload getPayloadFromIdToken(
            String token, OIDCPublicKeysResponse oidcPublicKeysResponse) {
        log.info("iss: " + iss);
        log.info("aud: " + aud);
        log.info("token: " + token);
        String kid = getKidFromUnsignedIdToken(token, iss, aud);
        // KakaoOauthHelper 에서 공개키를 조회했고 해당 디티오를 넘겨준다.
        OIDCPublicKeyDto oidcPublicKeyDto =
                oidcPublicKeysResponse.getKeys().stream()
                        // 같은 kid를 가져온다.
                        .filter(o -> o.getKid().equals(kid))
                        .findFirst()
                        .orElseThrow();
        // 검증이 된 토큰에서 바디를 꺼내온다.
        return jwtOIDCProvider.getOIDCTokenBody(
                token, oidcPublicKeyDto.getN(), oidcPublicKeyDto.getE());
    }

    public Optional<String> extractIdToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"));
    }

    public Optional<String> getIss(String idToken) {
        try {
            String[] parts = idToken.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid JWT token");
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payloadMap = mapper.readValue(payloadJson, Map.class);

            String issuer = (String) payloadMap.get("iss");
            return Optional.of(issuer);
        } catch (IOException e) {
            log.info(e.toString());
        }
        return Optional.empty();
    }

    public void setAuthentication(String idToken, String sub) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(sub);
        UsernamePasswordAuthenticationToken newAuth =
                new UsernamePasswordAuthenticationToken(userDetails, idToken, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}