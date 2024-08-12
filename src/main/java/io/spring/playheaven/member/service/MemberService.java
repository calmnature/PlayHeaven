package io.spring.playheaven.member.service;

import io.spring.playheaven.member.dto.LoginDto;
import io.spring.playheaven.member.dto.MemberRequestDto;
import io.spring.playheaven.member.dto.MemberResponseDto;
import io.spring.playheaven.member.entity.Member;
import io.spring.playheaven.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

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
}
