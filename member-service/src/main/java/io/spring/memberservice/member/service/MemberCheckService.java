package io.spring.memberservice.member.service;

import io.spring.memberservice.mail.service.MailService;
import io.spring.memberservice.member.dto.EmailAuthRequestDto;
import io.spring.memberservice.member.entity.Member;
import io.spring.memberservice.member.repository.MemberRepository;
import io.spring.memberservice.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberCheckService {
    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final RedisService redisService;

    public boolean emailOverlap(String email) {
        Member member = memberRepository.findByEmail(email);
        return member != null;
    }

    public boolean sendAuthCode(String email) {
        try{
            String authCode = mailService.sendSimpleMessage(email);
            redisService.setCode(email, authCode);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public boolean validationAuthCode(EmailAuthRequestDto emailAuthRequestDto) throws AuthenticationException {
        String savedCode = redisService.getCode(emailAuthRequestDto.getEmail());
        return emailAuthRequestDto.getAuthCode().equals(savedCode);
    }
}
