package io.spring.playheaven.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberRequestDto {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String phone;
}
