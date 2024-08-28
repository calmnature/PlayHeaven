package io.spring.gameservice.game.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GameResponseDetailDto {
    private Long gameId;
    private String gameName;
    private int price;
    private String detail;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;
}
