package io.spring.playheaven.game.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GameRegistDto {
    private String gameName;
    private int price;
    private String detail;
    private Long memberId;
}
