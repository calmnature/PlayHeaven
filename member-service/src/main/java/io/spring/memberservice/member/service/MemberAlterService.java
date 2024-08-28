package io.spring.memberservice.member.service;

import io.jsonwebtoken.Claims;
import io.spring.memberservice.jwt.JwtUtil;
import io.spring.memberservice.member.dto.AlterRequestDto;
import io.spring.memberservice.member.entity.Member;
import io.spring.memberservice.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberAlterService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional // Dirty Check로 업데이트
    public boolean update(AlterRequestDto alterRequestDto, HttpServletRequest req) {
        Member member = memberRepository.findById(getMemberId(req)).orElse(null);

        if(member != null && passwordEncoder.matches(alterRequestDto.getCurPassword(), member.getPassword())){
            alterRequestDto.setUpdatePassword(passwordEncoder.encode(alterRequestDto.getUpdatePassword()));
            member.patch(alterRequestDto);
            return true;
        }

        return false;
    }

    @Transactional
    public boolean delete(String curPassword, HttpServletRequest req) {
        Member member = memberRepository.findById(getMemberId(req)).orElse(null);

        if(member != null && passwordEncoder.matches(curPassword, member.getPassword())){
            member.changeDeleted();
            return true;
        }

        return false;
    }

    private Long getMemberId(HttpServletRequest req){
        String header = req.getHeader("Authorization");
        String token = jwtUtil.substringToken(header);
        Claims claims = jwtUtil.getTokenBody(token);
        return Long.parseLong(claims.get("memberId").toString());
    }
}
