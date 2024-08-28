package io.spring.gameservice.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameRegistDto {
    private String gameName;
    private int price;
    private String detail;
}
