package io.spring.playheaven.member.service;

import io.spring.playheaven.member.dto.LoginDto;
import io.spring.playheaven.member.dto.MemberRequestDto;
import io.spring.playheaven.member.dto.MemberResponseDto;
import io.spring.playheaven.member.entity.Auth;
import io.spring.playheaven.member.entity.Member;
import io.spring.playheaven.member.repository.AuthRepository;
import io.spring.playheaven.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final AuthRepository authRepository;

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

    @Transactional
    public boolean sendAuthcode(String email) throws MessagingException {
        String authCode = mailService.sendSimpleMessage(email);
        if(authCode != null){
            Auth auth = authRepository.findByEmail(email);
            if(auth == null)
                authRepository.save(new Auth(email, authCode));
            else
                auth.patch(authCode);
            return true;
        }
        return false;
    }

    public boolean validationAuthcode(String email, String authCode) {
        Auth auth = authRepository.findByEmail(email);
        if(auth != null && auth.getAuthCode().equals(authCode)){
            authRepository.delete(auth);
            return true;
        }
        return false;
    }
}
