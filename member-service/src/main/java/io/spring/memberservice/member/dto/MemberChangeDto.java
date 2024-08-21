package io.spring.memberservice.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberChangeDto {
    private Long memberId;
    private String curPassword;
    private String updatePassword;
    private String nickname;
}
