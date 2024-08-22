package io.spring.memberservice.member.service;

import io.spring.memberservice.member.dto.LoginDto;
import io.spring.memberservice.member.dto.MemberChangeDto;
import io.spring.memberservice.member.dto.MemberRegistDto;
import io.spring.memberservice.member.dto.MemberResponseDto;
import io.spring.memberservice.member.entity.Member;
import io.spring.memberservice.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void regist(MemberRegistDto memberRequestDto) {
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
