package com.minpaeng.careroute.domain.member.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {
    private final OauthOIDCHelper oauthOIDCHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("인증필더 진입 doFilterInternal");

        // 헤더에서 JWT 받기
        String token = oauthOIDCHelper.extractIdToken(request).orElse(null);
        // 유효한 토큰인지 확인
        if (token != null) {
            OIDCDecodePayload oidcDecodePayload = oauthOIDCHelper.getOIDCDecodePayload(token);
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받기
            Authentication authentication = oauthOIDCHelper.getAuthentication(token, oidcDecodePayload.getSub());
            // SecurityContext 에 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
