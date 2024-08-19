package io.spring.playheaven.wishlist.controller;

import io.spring.playheaven.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mypage/wishlist")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;

    @GetMapping("/regist")
    public ResponseEntity<String> regist(@RequestParam(name = "memberId")Long memberId,
                                         @RequestParam(name = "gameId")Long gameId){
        boolean success = wishlistService.regist(memberId, gameId);
        return success ? ResponseEntity.status(HttpStatus.OK).body("위시리스트 등록에 성공하였습니다.") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("위시리스트 등록에 실패하였습니다.");
    }
}
