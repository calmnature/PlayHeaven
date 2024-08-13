package io.spring.playheaven.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateDto {
    private Long memberId;
    private String curPassword;
    private String updatePassword;
    private String nickname;
}
