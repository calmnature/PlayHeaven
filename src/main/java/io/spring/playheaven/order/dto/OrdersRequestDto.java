package io.spring.playheaven.order.dto;

import io.spring.playheaven.game.entity.Game;
import io.spring.playheaven.member.entity.Member;
import lombok.Getter;

import java.util.List;

@Getter
public class OrdersRequestDto {
    private Long memberId;
    private List<Game> gameList;

    public int getTotalPrice(){
        return gameList.stream()
                .mapToInt(Game::getPrice)
                .sum();
    }
}
