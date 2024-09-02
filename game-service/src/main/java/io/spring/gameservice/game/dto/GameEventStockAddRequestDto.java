package io.spring.gameservice.game.dto;

import lombok.Getter;

@Getter
public class GameEventStockAddRequestDto {
    private Long gameId;
    private int eventStock;
}
