package io.spring.playheaven.game.service;

import io.spring.playheaven.game.dto.GameRegistDto;
import io.spring.playheaven.game.entity.Game;
import io.spring.playheaven.game.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;

    public boolean regist(GameRegistDto gameRegistDto) {
        Game existed = gameRepository.findByGameName(gameRegistDto.getGameName());
        if(existed != null)
            return false;
        gameRepository.save(Game.toEntity(gameRegistDto));
        return true;
    }
}
