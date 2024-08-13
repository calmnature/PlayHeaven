package io.spring.playheaven.member.service;

import io.spring.playheaven.member.dto.LoginDto;
import io.spring.playheaven.member.dto.MemberRequestDto;
import io.spring.playheaven.member.dto.MemberResponseDto;
import io.spring.playheaven.member.entity.Member;
import io.spring.playheaven.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final RedisService redisService;

    public boolean emailOverlap(String email) {
        Member member = memberRepository.findByEmail(email);
        return member != null;
    }

    public boolean nicknameOverlap(String nickname) {
        Member member = memberRepository.findByNickname(nickname);
        return member != null;
    }

    public void regist(MemberRequestDto memberRequestDto) {
        memberRepository.save(Member.toEntity(memberRequestDto));
    }

    public MemberResponseDto login(LoginDto loginDto) {
        Member member = memberRepository.findByEmail(loginDto.getEmail());
        if(member != null && member.getPassword().equals(loginDto.getPassword()))
            return new MemberResponseDto(member);
        return null;
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
}
