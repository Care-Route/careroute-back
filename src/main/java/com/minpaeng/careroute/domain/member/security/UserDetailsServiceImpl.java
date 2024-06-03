package com.minpaeng.careroute.domain.member.security;

import com.minpaeng.careroute.domain.member.repository.MemberRepository;
import com.minpaeng.careroute.domain.member.repository.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String socialId) throws UsernameNotFoundException {
        Member member = memberRepository.findMemberBySocialId(socialId)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 사용자가 없습니다."));
        return UserDetailsImpl.builder()
                .memberRole(member.getRole().getValue())
                .socialId(member.getSocialId())
                .socialType(member.getSocialType().getValue())
                .build();
    }
}
