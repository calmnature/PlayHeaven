package io.spring.gameservice.game.service;


import io.spring.gameservice.game.dto.*;
import io.spring.gameservice.game.entity.Game;
import io.spring.gameservice.game.repository.GameRepository;
import io.spring.gameservice.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "GameService")
public class GameService {
    private final GameRepository gameRepository;
    private final JwtUtil jwtUtil;

    public boolean regist(GameRegistDto gameRegistDto, HttpServletRequest req) {
        Game existed = gameRepository.findByGameName(gameRegistDto.getGameName());

        if(existed != null)
            return false;

        gameRepository.save(
                Game.builder()
                        .gameName(gameRegistDto.getGameName())
                        .price(gameRegistDto.getPrice())
                        .detail(gameRegistDto.getDetail())
                        .memberId(getMemberId(req))
                        .saled(true)
                        .build()
        );
        return true;
    }

    @Transactional
    public boolean addEventStock(GameEventStockAddRequestDto gameEventStockRequestDto, HttpServletRequest req) {
        Optional<Game> optionalGame = gameRepository.findById(gameEventStockRequestDto.getGameId());
        if(optionalGame.isPresent()){
            Game game  = optionalGame.get();
            if(game.getMemberId().equals(getMemberId(req))){
                game.addEventStock(gameEventStockRequestDto.getEventStock());
                return true;
            }
        }
        return false;
    }

    public List<GameResponseDto> list(int pageNo, int size) {
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<Game> page = gameRepository.findAllBySaledIsTrue(pageable);

        return page.getContent().stream()
                        .map(GameResponseDto::new)
                        .toList();
    }

    public GameResponseDetailDto detail(Long gameId) {
        Optional<Game> optionalGame = gameRepository.findByGameIdAndSaledIsTrue(gameId);
        if(optionalGame.isPresent()){
            Game game = optionalGame.get();
            return GameResponseDetailDto.builder()
                    .gameId(game.getGameId())
                    .gameName(game.getGameName())
                    .price(game.getPrice())
                    .detail(game.getDetail())
                    .createAt(game.getCreateAt())
                    .modifyAt(game.getModifyAt())
                    .build();
        }
        return null;
    }

    public GameDto findById(Long gameId) {
        Game game = gameRepository.findById(gameId).orElse(null);
        if(game == null) return null;

        return GameDto.toDto(game);
    }

    public List<GameDto> subFind(List<Long> gameIdList) {
        List<Game> gameList = gameRepository.findAllByGameIdIn(gameIdList);
        return gameList.stream()
                .map(GameDto::toDto)
                .toList();
    }

    private Long getMemberId(HttpServletRequest req){
        String header = req.getHeader("Authorization");
        String token = jwtUtil.substringToken(header);
        return Long.parseLong(jwtUtil.getTokenBody(token).get("memberId").toString());
    }
}
