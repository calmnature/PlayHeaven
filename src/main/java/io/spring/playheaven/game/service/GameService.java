package io.spring.playheaven.game.service;

import io.spring.playheaven.game.dto.GameRegistDto;
import io.spring.playheaven.game.dto.GameResponseDto;
import io.spring.playheaven.game.entity.Game;
import io.spring.playheaven.game.repository.GameRepository;
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
}
