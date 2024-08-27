package io.spring.memberservice.member.entity;

import io.spring.memberservice.jwt.UserRole;
import io.spring.memberservice.member.dto.MemberChangeDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @ColumnDefault("0")
    private UserRole role;

    @ColumnDefault("false")
    private Boolean deleted;

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
