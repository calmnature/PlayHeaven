package io.spring.memberservice.member.entity;

import io.spring.memberservice.jwt.UserRole;
import io.spring.memberservice.member.dto.AlterRequestDto;
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

    public void patch(AlterRequestDto alterRequestDto) {
        if(alterRequestDto.getUpdatePassword() != null)
            this.password = alterRequestDto.getUpdatePassword();
        if(alterRequestDto.getNickname() != null)
            this.nickname = alterRequestDto.getNickname();
    }

    public void changeDeleted() {
        this.deleted = !this.deleted;
    }
}
