package io.spring.orderservice.member.dto;

import io.spring.orderservice.member.entity.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberResponseDto {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String nickname;
    private boolean isDeleted;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;

    public MemberResponseDto(Member member){
        this.id = member.getMemberId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.name = member.getName();
        this.nickname = member.getNickname();
        this.phone = member.getPhone();
        this.createAt = member.getCreateAt();
        this.modifyAt = member.getModifyAt();
    }
}
