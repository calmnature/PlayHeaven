package io.spring.gameservice.game.dto;

import io.spring.gameservice.game.entity.Game;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameDto {
    private Long gameId;
    private String gameName;
    private int price;
    private int eventStock;
    private String detail;
    private boolean saled;
    private Long memberId;

    public static GameDto toDto(Game game){
        return GameDto.builder()
                .gameId(game.getGameId())
                .gameName(game.getGameName())
                .price(game.getPrice())
                .eventStock(game.getEventStock())
                .detail(game.getDetail())
                .saled(game.isSaled())
                .memberId(game.getMemberId())
                .build();
    }
}
