package io.spring.gameservice.member.service;

import io.spring.gameservice.member.dto.LoginDto;
import io.spring.gameservice.member.dto.MemberChangeDto;
import io.spring.gameservice.member.dto.MemberRegistDto;
import io.spring.gameservice.member.dto.MemberResponseDto;
import io.spring.gameservice.member.entity.Member;
import io.spring.gameservice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;

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

    public void regist(MemberRegistDto memberRequestDto) {
        memberRepository.save(Member.toEntity(memberRequestDto));
    }

    public MemberResponseDto login(LoginDto loginDto) {
        Member member = memberRepository.findByEmail(loginDto.getEmail());
        if(member != null && member.getPassword().equals(loginDto.getPassword()))
            return new MemberResponseDto(member);
        return null;
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
