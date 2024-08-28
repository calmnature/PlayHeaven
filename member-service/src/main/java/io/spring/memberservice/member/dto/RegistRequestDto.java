package io.spring.memberservice.member.dto;

import io.spring.memberservice.jwt.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegistRequestDto {
    private String email;
    @Setter
    private String password;
    private String name;
    private String nickname;
    private String phone;
    private UserRole role;
    private boolean emailAuth;
}
