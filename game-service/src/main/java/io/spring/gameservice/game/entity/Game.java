package io.spring.gameservice.game.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Game extends BaseTime{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;
    private String gameName;
    private int price;
    private int eventStock;
    private String detail;
    private boolean saled;
    private Long memberId;

    public void addEventStock(int addStock){
        this.eventStock += addStock;
    }

    public void increasement(){
        this.eventStock++;
    }

    public void decreasement(){
        this.eventStock--;
    }
}
