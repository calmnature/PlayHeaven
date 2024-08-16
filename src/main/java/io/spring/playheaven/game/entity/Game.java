package io.spring.playheaven.game.entity;

import io.spring.playheaven.game.dto.GameRegistDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game extends BaseTime{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;
    private String gameName;
    private int price;
    private int eventStock;
    private String detail;
    private boolean saled;
    private Long memberId;

    public static Game toEntity(GameRegistDto gameRegistDto){
        return new Game(
                null,
                gameRegistDto.getGameName(),
                gameRegistDto.getPrice(),
                0,
                gameRegistDto.getDetail(),
                true,
                gameRegistDto.getMemberId()
        );
    }
}
