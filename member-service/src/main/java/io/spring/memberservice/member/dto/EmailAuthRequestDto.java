package io.spring.memberservice.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailAuthRequestDto {
    private String email;
    private String authCode;

}
