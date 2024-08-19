package io.spring.playheaven.ordersgame.entity;

import io.spring.playheaven.game.entity.Game;
import io.spring.playheaven.order.entity.Orders;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@ToString
public class OrdersGame {
    @Id @GeneratedValue
    private Long ordersGameId;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    public OrdersGame(Orders orders, Game game){
        this.price = game.getPrice();
        this.orders = orders;
        this.game = game;
    }
}
