package io.spring.playheaven.game.dto;

import io.spring.playheaven.game.entity.Game;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GameResponseDetailDto {
    private Long gameId;
    private String gameName;
    private int price;
    private String detail;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;

    public GameResponseDetailDto(Game game){
        this.gameId = game.getId();
        this.gameName = game.getGameName();
        this.price = game.getPrice();
        this.detail = game.getDetail();
        this.createAt = game.getCreateAt();
        this.modifyAt = game.getModifyAt();
    }
}
