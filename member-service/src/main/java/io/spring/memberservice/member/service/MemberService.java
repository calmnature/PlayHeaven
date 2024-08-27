package io.spring.memberservice.member.service;

import io.spring.memberservice.jwt.JwtUtil;
import io.spring.memberservice.jwt.UserRole;
import io.spring.memberservice.member.dto.LoginDto;
import io.spring.memberservice.member.dto.MemberChangeDto;
import io.spring.memberservice.member.dto.MemberRegistDto;
import io.spring.memberservice.member.entity.Member;
import io.spring.memberservice.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final RedisService redisService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public boolean emailOverlap(String email) {
        Member member = memberRepository.findByEmail(email);
        return member != null;
    }

    public boolean nicknameOverlap(String nickname) {
        Member member = memberRepository.findByNickname(nickname);
        return member != null;
    }

    public boolean regist(MemberRegistDto memberRequestDto) {
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

    public boolean login(LoginDto loginDto, HttpServletResponse res) {
        Member member = memberRepository.findByEmail(loginDto.getEmail());
        if(member == null || passwordEncoder.matches(member.getPassword(), loginDto.getPassword()))
            return false;

        String token = jwtUtil.createToken(member);
        jwtUtil.addJwtToHeader(token, res);
        return true;
    }

    public boolean sendAuthcode(String email) throws MessagingException {
        String authCode = mailService.sendSimpleMessage(email);
        redisService.setCode(email, authCode);
        return true;
    }

    public boolean validationAuthcode(String email, String authCode) throws AuthenticationException {
        String savedCode = redisService.getCode(email);
        return authCode.equals(savedCode);
    }

    @Transactional // Dirty Check로 업데이트
    public boolean update(MemberChangeDto updateDto) {
        Member member = memberRepository.findById(updateDto.getMemberId()).orElse(null);

        if(member != null && updateDto.getCurPassword().equals(member.getPassword())){
            member.patch(updateDto);
            return true;
        }

        return false;
    }

    @Transactional
    public boolean delete(MemberChangeDto deleteDto) {
        Member member = memberRepository.findById(deleteDto.getMemberId()).orElse(null);

        if(member != null && deleteDto.getCurPassword().equals(member.getPassword())){
            member.changeDeleted();
            return true;
        }

        return false;
    }
}
