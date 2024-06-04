package com.minpaeng.careroute.domain.member.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Builder
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    @Getter
    private String socialType;
    private String socialId;
    private String memberRole;

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return socialId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (memberRole != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + memberRole));
        }
        return authorities;
    }
}
