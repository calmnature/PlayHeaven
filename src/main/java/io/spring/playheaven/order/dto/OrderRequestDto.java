package io.spring.playheaven.order.dto;

import io.spring.playheaven.game.entity.Game;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequestDto {
    private Long memberId;
    private List<Game> gameList;

    public int getTotalPrice(){
        return gameList.stream()
                .mapToInt(Game::getPrice)
                .sum();
    }
}
