package com.minpaeng.careroute.domain.member.repository.redis;

import com.minpaeng.careroute.domain.member.security.OIDCPublicKeysResponse;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OIDCPublicKeysRepository extends CrudRepository<OIDCPublicKeysResponse, Long> {
    Optional<OIDCPublicKeysResponse> findOIDCPublicKeysResponseByName(String name);
}
