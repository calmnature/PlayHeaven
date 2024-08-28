package io.spring.memberservice.member.controller;

import io.spring.memberservice.member.dto.AlterRequestDto;
import io.spring.memberservice.member.dto.EmailAuthRequestDto;
import io.spring.memberservice.member.dto.LoginRequestDto;
import io.spring.memberservice.member.dto.RegistRequestDto;
import io.spring.memberservice.member.service.MemberAlterService;
import io.spring.memberservice.member.service.MemberCheckService;
import io.spring.memberservice.member.service.MemberDefaultService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Slf4j(topic = "MemberController")
public class MemberController {
    private final MemberCheckService memberCheckService;
    private final MemberDefaultService memberDefaultService;
    private final MemberAlterService memberAlterService;

    @GetMapping("/email-overlap/{email}")
    public ResponseEntity<String> emailOverlap(@PathVariable String email){
        boolean overlap = memberCheckService.emailOverlap(email);

        return !overlap ? ResponseEntity.status(HttpStatus.OK).body("사용 가능한 이메일 입니다.") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용 불가능한 이메일 입니다.");
    }

    @GetMapping("/email-auth/{email}")
    public ResponseEntity<String> requestAuthCode(@PathVariable String email) {
        boolean isSend = memberCheckService.sendAuthCode(email);
        return isSend ? ResponseEntity.status(HttpStatus.OK).body("인증 코드가 전송되었습니다.") :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인증 코드 전송에 실패하였습니다.");
    }

    @PostMapping("/email-auth")
    public ResponseEntity<String> validateAuthCode(@RequestBody EmailAuthRequestDto emailAuthRequestDto) throws AuthenticationException {
        boolean isSuccess = memberCheckService.validationAuthCode(emailAuthRequestDto);
        return isSuccess ? ResponseEntity.status(HttpStatus.OK).body("이메일 인증에 성공하였습니다.") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 인증에 실패하였습니다.");
    }

    @PostMapping("/regist")
    public ResponseEntity<String> regist(@RequestBody RegistRequestDto memberRequestDto){
        boolean success = memberDefaultService.regist(memberRequestDto);
        return success ? ResponseEntity.status(HttpStatus.CREATED).body("회원 가입에 성공하였습니다.") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 가입에 실패하였습니다.\n이메일 및 닉네임 중복 확인을 해주세요.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginDto, HttpServletResponse res) {
        boolean success = memberDefaultService.login(loginDto, res);

        return success ? ResponseEntity.status(HttpStatus.OK).body("로그인에 성공하였습니다.") :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인에 실패하였습니다.\n아이디 또는 비밀번호를 확인해주세요.");
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest req){
        boolean success = memberDefaultService.logout(req);
        return success ? ResponseEntity.status(HttpStatus.OK).body("로그아웃 되었습니다.") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그아웃 할 수 없습니다.");
    }

    @PatchMapping("/update")
    public ResponseEntity<String> update(@RequestBody AlterRequestDto updateDto,
                                         HttpServletRequest req){
        boolean success = memberAlterService.update(updateDto, req);
        return success ? ResponseEntity.status(HttpStatus.OK).body("회원 정보가 수정되었습니다.") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 정보 변경에 실패하였습니다.\n현재 비밀번호를 다시 확인바랍니다.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam String curPassword,
                                         HttpServletRequest req){
        boolean success = memberAlterService.delete(curPassword, req);
        return success ? ResponseEntity.status(HttpStatus.OK).body("회원 탈퇴에 성공하였습니다.") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 탈퇴에 실패하였습니다.\n비밀번호를 다시 확인바랍니다.");
    }
}
