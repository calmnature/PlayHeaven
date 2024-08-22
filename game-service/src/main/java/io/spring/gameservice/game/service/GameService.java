package io.spring.gameservice.game.service;

import io.spring.gameservice.game.dto.GameRegistDto;
import io.spring.gameservice.game.dto.GameResponseDetailDto;
import io.spring.gameservice.game.dto.GameResponseDto;
import io.spring.gameservice.game.entity.Game;
import io.spring.gameservice.game.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "GameService")
public class GameService {
    private final GameRepository gameRepository;

    public boolean regist(GameRegistDto gameRegistDto) {
        Game existed = gameRepository.findByGameName(gameRegistDto.getGameName());
        if(existed != null)
            return false;
        gameRepository.save(Game.toEntity(gameRegistDto));
        return true;
    }

    public ResponseEntity<List<GameResponseDto>> allList(int pageNo, int size) {
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<Game> page = gameRepository.findAllBySaledIsTrue(pageable);

        return ResponseEntity.ok(
                page.getContent().stream()
                        .map(GameResponseDto::new)
                        .collect(Collectors.toList())
        );
    }

    public GameResponseDetailDto detail(Long gameId) {
        Game game = gameRepository.findByGameIdAndSaledIsTrue(gameId).orElse(null);
        if(game != null)
            return new GameResponseDetailDto(game);
        return null;
    }
}
