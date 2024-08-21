package io.spring.orderservice.order.entity;

import io.spring.orderservice.game.entity.Game;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@ToString
public class OrderGame {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderGameId;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    public OrderGame(Order order, Game game){
        this.price = game.getPrice();
        this.order = order;
        this.game = game;
    }
}
