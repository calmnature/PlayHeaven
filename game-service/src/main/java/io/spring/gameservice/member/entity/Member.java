package io.spring.gameservice.member.entity;

import io.spring.gameservice.member.dto.MemberChangeDto;
import io.spring.gameservice.member.dto.MemberRegistDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    private Boolean deleted;

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
        this.deleted = !this.deleted;
    }
}
