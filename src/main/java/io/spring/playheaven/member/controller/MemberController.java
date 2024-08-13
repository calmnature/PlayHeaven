package io.spring.playheaven.member.controller;

import io.spring.playheaven.member.dto.LoginDto;
import io.spring.playheaven.member.dto.MemberRequestDto;
import io.spring.playheaven.member.dto.MemberResponseDto;
import io.spring.playheaven.member.service.MailService;
import io.spring.playheaven.member.service.MemberService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j(topic = "MemberController")
public class MemberController {
    private final MemberService memberService;
    private final MailService mailService;

    @GetMapping("/email/overlap/{email}")
    public ResponseEntity<String> emailOverlap(@PathVariable String email){
        boolean overlap = memberService.emailOverlap(email);

        return overlap ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용 불가능한 이메일 입니다.") :
                ResponseEntity.status(HttpStatus.OK).body("사용 가능한 이메일 입니다.");
    }

    @GetMapping("/email/auth/{email}")
    public ResponseEntity<String> requestAuthcode(@PathVariable String email) throws MessagingException {
        boolean isSend = mailService.sendSimpleMessage(email);
        return isSend ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 코드가 전송되었습니다.") :
                ResponseEntity.status(HttpStatus.OK).body("인증 코드 발급에 실패하였습니다.");
    }

    @GetMapping("/nickname/overlap/{nickname}")
    public ResponseEntity<String> nicknameOverlap(@PathVariable String nickname){
        log.info("닉네임 중복 체크 = {}", nickname);
        boolean overlap = memberService.nicknameOverlap(nickname);

        return overlap ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용 불가능한 닉네임 입니다.") :
                ResponseEntity.status(HttpStatus.OK).body("사용 가능한 닉네임 입니다.");
    }

    @PostMapping("/regist")
    public ResponseEntity<String> regist(@RequestBody MemberRequestDto memberRequestDto){
        memberService.regist(memberRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원 가입에 성공하였습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        MemberResponseDto responseDto = memberService.login(loginDto);

        if(responseDto == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인에 실패하였습니다.");
        }

        HttpSession session = request.getSession();
        session.setAttribute("member", responseDto);
        return ResponseEntity.status(HttpStatus.OK).body("로그인에 성공하였습니다.");
    }
}
