package io.spring.orderservice.game.dto;

import io.spring.orderservice.game.entity.Game;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GameResponseDto {
    private Long gameId;
    private String gameName;
    private int price;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;

    public GameResponseDto(Game game){
        this.gameId = game.getGameId();
        this.gameName = game.getGameName();
        this.price = game.getPrice();
        this.createAt = game.getCreateAt();
        this.modifyAt = game.getModifyAt();
    }
}
