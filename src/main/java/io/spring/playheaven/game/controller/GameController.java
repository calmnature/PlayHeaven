package io.spring.playheaven.game.controller;

import io.spring.playheaven.game.dto.GameRegistDto;
import io.spring.playheaven.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/game")
public class GameController {
    private final GameService gameService;

    @PostMapping("/regist")
    public ResponseEntity<String> regist(@RequestBody GameRegistDto gameRegistDto){
        boolean success = gameService.regist(gameRegistDto);
        return success ? ResponseEntity.status(HttpStatus.CREATED).body("판매 게임 등록에 성공하였습니다.") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("판매 게임 등록에 실패하였습니다.");
    }
}
