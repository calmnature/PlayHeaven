package io.spring.playheaven.member.entity;

import io.spring.playheaven.member.dto.MemberRegistDto;
import io.spring.playheaven.member.dto.MemberChangeDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String phone;

    private Boolean isDeleted;

    public static Member toEntity(MemberRegistDto memberRequestDto){
        return new Member(
                null,
                memberRequestDto.getEmail(),
                memberRequestDto.getPassword(),
                memberRequestDto.getName(),
                memberRequestDto.getNickname(),
                memberRequestDto.getPhone(),
                false
        );
    }

    public void patch(MemberChangeDto updateDto) {
        if(updateDto.getUpdatePassword() != null)
            this.password = updateDto.getUpdatePassword();
        if(updateDto.getNickname() != null)
            this.nickname = updateDto.getNickname();
    }

    public void changeDeleted() {
        this.isDeleted = !this.isDeleted;
    }
}
