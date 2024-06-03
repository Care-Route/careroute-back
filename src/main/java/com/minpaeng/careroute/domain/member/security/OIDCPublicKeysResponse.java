package com.minpaeng.careroute.domain.member.security;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("KakaoOIDC")
public class OIDCPublicKeysResponse implements Serializable {
    @Id
    Long id;
    @Indexed
    String name = "KakaoOIDC";
    List<OIDCPublicKeyDto> keys;
}
