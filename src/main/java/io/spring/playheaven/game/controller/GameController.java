package io.spring.playheaven.game.controller;

import io.spring.playheaven.game.dto.GameRegistDto;
import io.spring.playheaven.game.dto.GameResponseDetailDto;
import io.spring.playheaven.game.dto.GameResponseDto;
import io.spring.playheaven.game.entity.Game;
import io.spring.playheaven.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/all")
    public ResponseEntity<List<GameResponseDto>> allList(@RequestParam(name = "pageNo", required = false, defaultValue = "1")int pageNo,
                                                         @RequestParam(name = "size", required = false, defaultValue = "10")int size){
        return gameService.allList(pageNo - 1, size);
    }

    @GetMapping("/detail/{gameId}")
    public ResponseEntity<GameResponseDetailDto> detail(@PathVariable Long gameId){
        GameResponseDetailDto responseDto = gameService.detail(gameId);
        return responseDto == null ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build() :
                ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
