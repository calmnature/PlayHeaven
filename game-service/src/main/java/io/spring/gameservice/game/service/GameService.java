package io.spring.gameservice.game.service;

import io.spring.gameservice.game.dto.GameDto;
import io.spring.gameservice.game.dto.GameRegistDto;
import io.spring.gameservice.game.dto.GameResponseDetailDto;
import io.spring.gameservice.game.dto.GameResponseDto;
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
        String header = req.getHeader("Authorization");
        String token = jwtUtil.substringToken(header);
        Long memberId = Long.parseLong(jwtUtil.getTokenBody(token).get("memberId").toString());

        if(existed != null)
            return false;

        gameRepository.save(
                Game.builder()
                        .gameName(gameRegistDto.getGameName())
                        .price(gameRegistDto.getPrice())
                        .detail(gameRegistDto.getDetail())
                        .memberId(memberId)
                        .saled(true)
                        .build()
        );
        return true;
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
}
