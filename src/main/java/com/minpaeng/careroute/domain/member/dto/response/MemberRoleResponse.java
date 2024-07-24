package com.minpaeng.careroute.domain.member.dto.response;

import com.minpaeng.careroute.domain.member.repository.entity.enums.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberRoleResponse {
    private MemberRole role;
}
