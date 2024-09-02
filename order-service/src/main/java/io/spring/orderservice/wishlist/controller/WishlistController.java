package io.spring.orderservice.wishlist.controller;

import io.spring.orderservice.wishlist.dto.WishlistResponseDto;
import io.spring.orderservice.wishlist.service.WishlistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;

    @GetMapping("/regist/{gameId}")
    public ResponseEntity<String> regist(@PathVariable(name = "gameId")Long gameId,
                                         HttpServletRequest req){
        boolean success = wishlistService.regist(gameId, req);
        return success ? ResponseEntity.status(HttpStatus.OK).body("위시리스트 등록에 성공하였습니다.") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("위시리스트 등록에 실패하였습니다.");
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                                  @RequestParam(name = "size",defaultValue = "10")int size,
                                  HttpServletRequest req){
        WishlistResponseDto wishlistResponseDto = wishlistService.list(pageNo - 1, size, req);
        return wishlistResponseDto != null ? ResponseEntity.status(HttpStatus.OK).body(wishlistResponseDto) :
                                                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("위시리스트 조회에 실패하였습니다.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam(name = "gameId")Long gameId,
                                         HttpServletRequest req){
        boolean success = wishlistService.delete(gameId, req);
        return success ? ResponseEntity.status(HttpStatus.OK).body("위시리스트 삭제에 성공하였습니다.") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("위시리스트 삭제에 실패하였습니다.");
    }
}
