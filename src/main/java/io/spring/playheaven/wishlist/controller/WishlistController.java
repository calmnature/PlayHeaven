package io.spring.playheaven.wishlist.controller;

import io.spring.playheaven.wishlist.dto.WishlistResponseDto;
import io.spring.playheaven.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/list/{memberId}")
    public ResponseEntity<WishlistResponseDto> list(@PathVariable(name = "memberId") Long memberId,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                                                    @RequestParam(name = "size",defaultValue = "10")int size){
        WishlistResponseDto wishlistResponseDto = wishlistService.list(memberId, pageNo - 1, size);
        return wishlistResponseDto != null ? ResponseEntity.status(HttpStatus.OK).body(wishlistResponseDto) :
                                                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam(name = "memberId")Long memberId,
                                         @RequestParam(name = "gameId")Long gameId){
        boolean success = wishlistService.delete(memberId, gameId);
        return success ? ResponseEntity.status(HttpStatus.OK).body("위시리스트 삭제에 성공하였습니다.") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("위시리스트 삭제에 실패하였습니다.");
    }
}
