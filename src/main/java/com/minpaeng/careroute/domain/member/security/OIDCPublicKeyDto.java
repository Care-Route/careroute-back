package com.minpaeng.careroute.domain.member.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class OIDCPublicKeyDto {
    private String kid;
    private String kty;
    private String alg;
    private String use;
    private String n;
    private String e;
}