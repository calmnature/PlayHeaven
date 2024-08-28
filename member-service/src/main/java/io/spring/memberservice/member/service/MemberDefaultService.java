package io.spring.memberservice.member.service;

import io.spring.memberservice.jwt.JwtUtil;
import io.spring.memberservice.jwt.UserRole;
import io.spring.memberservice.member.dto.LoginRequestDto;
import io.spring.memberservice.member.dto.RegistRequestDto;
import io.spring.memberservice.member.entity.Member;
import io.spring.memberservice.member.repository.MemberRepository;
import io.spring.memberservice.redis.service.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberDefaultService {
    private final MemberRepository memberRepository;
    private final RedisService redisService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public boolean regist(RegistRequestDto memberRequestDto) {
        Member member = memberRepository.findByEmail(memberRequestDto.getEmail());
        if(!memberRequestDto.isEmailAuth() || member != null)
            return false;

        memberRequestDto.setPassword(passwordEncoder.encode(memberRequestDto.getPassword()));
        memberRepository.save(
                Member.builder()
                        .email(memberRequestDto.getEmail())
                        .password(memberRequestDto.getPassword())
                        .name(memberRequestDto.getName())
                        .nickname(memberRequestDto.getNickname())
                        .deleted(false)
                        .phone(memberRequestDto.getPhone())
                        .role(UserRole.USER)
                        .build()
        );
        return true;
    }

    public boolean login(LoginRequestDto loginDto, HttpServletResponse res) {
        Member member = memberRepository.findByEmail(loginDto.getEmail());
        if(member == null || !passwordEncoder.matches(loginDto.getPassword(), member.getPassword()))
            return false;

        // Access Token 생성하여 Response Header에 추가
        String accessToken = jwtUtil.createAccessToken(member);
        jwtUtil.addJwtToHeader(accessToken, res);

        // Refresh Token 생성하여 redis에 저장
        String refreshToken = jwtUtil.createRefreshToken(member);
        redisService.saveRefreshToken(member.getEmail(), refreshToken);
        return true;
    }

    public boolean logout(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        String token = jwtUtil.substringToken(header);

        String subject = jwtUtil.getTokenBody(token).getSubject();
        return redisService.deleteRefreshToken(subject);
    }
}
