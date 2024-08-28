package io.spring.memberservice.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AlterRequestDto {
    private String curPassword;
    @Setter
    private String updatePassword;
    private String nickname;
}
