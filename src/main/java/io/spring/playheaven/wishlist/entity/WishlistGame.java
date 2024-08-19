package io.spring.playheaven.wishlist.entity;

import io.spring.playheaven.game.entity.Game;
import io.spring.playheaven.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistGame {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistGameId;
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_id")
    private Wishlist wishlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;
}
