package com.minpaeng.careroute.domain.member.dto;

import com.minpaeng.careroute.domain.member.repository.entity.Member;
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
@RedisHash(value = "connection", timeToLive = 600)
public class ConnectionDto {
    @Id
    private String id;
    @Indexed
    private String toNumber;
    @Indexed
    private String fromNumber;
    private String code;
}
