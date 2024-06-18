package com.minpaeng.careroute.domain.member.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        String token = oauthOIDCHelper.extractIdToken(request).orElse(null);

        if (token != null) {
            OIDCDecodePayload oidcDecodePayload = oauthOIDCHelper.getOIDCDecodePayload(token);
            // 토큰이 유효하면 유저 정보를 받아 SecurityContext에 Authentication 객체를 저장
            oauthOIDCHelper.setAuthentication(token, oidcDecodePayload.getSub());
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/api/members/login");
    }
}
