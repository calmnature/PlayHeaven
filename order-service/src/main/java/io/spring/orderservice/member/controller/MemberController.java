package io.spring.orderservice.member.controller;

import io.spring.orderservice.member.dto.LoginDto;
import io.spring.orderservice.member.dto.MemberChangeDto;
import io.spring.orderservice.member.dto.MemberRegistDto;
import io.spring.orderservice.member.dto.MemberResponseDto;
import io.spring.orderservice.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j(topic = "MemberController")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/email/overlap/{email}")
    public ResponseEntity<String> emailOverlap(@PathVariable String email){
        boolean overlap = memberService.emailOverlap(email);

        return overlap ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용 불가능한 이메일 입니다.") :
                ResponseEntity.status(HttpStatus.OK).body("사용 가능한 이메일 입니다.");
    }

    @GetMapping("/nickname/overlap/{nickname}")
    public ResponseEntity<String> nicknameOverlap(@PathVariable String nickname){
        log.info("닉네임 중복 체크 = {}", nickname);
        boolean overlap = memberService.nicknameOverlap(nickname);

        return overlap ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용 불가능한 닉네임 입니다.") :
                ResponseEntity.status(HttpStatus.OK).body("사용 가능한 닉네임 입니다.");
    }

    @PostMapping("/regist")
    public ResponseEntity<String> regist(@RequestBody MemberRegistDto memberRequestDto){
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

    @PatchMapping("/update")
    public ResponseEntity<String> update(@RequestBody MemberChangeDto updateDto){
        boolean success = memberService.update(updateDto);
        return success ? ResponseEntity.status(HttpStatus.OK).body("회원 정보가 수정되었습니다.") :
                ResponseEntity.status(HttpStatus.OK).body("회원 정보 변경에 실패하였습니다.\n현재 비밀번호를 다시 확인바랍니다.");
    }

    @PatchMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody MemberChangeDto deleteDto){
        boolean success = memberService.delete(deleteDto);
        return success ? ResponseEntity.status(HttpStatus.OK).body("회원 탈퇴에 성공하였습니다.") :
                ResponseEntity.status(HttpStatus.OK).body("회원 탈퇴에 실패하였습니다.\n비밀번호를 다시 확인바랍니다.");
    }
}
