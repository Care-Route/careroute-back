package com.minpaeng.careroute.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "phoneAuth", timeToLive = 180)
public class PhoneAuthDto {
    @Id
    private String id;
    @Indexed
    private int memberId;
    @Indexed
    private String phoneNumber;
    private String code;
}
